package com.itmo.dragon.server;

import com.itmo.dragon.shared.entities.*;

import java.sql.*;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Hashtable;

public class DatabaseConnection {
    String user = "postgres";
    String password = "postgres";
    String url = "jdbc:postgresql://localhost:5432/postgres";

    public Hashtable<Long, Dragon> getDragons() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);

            String query = """
                    Select\s
                    dragon.user_id,
                    dragon.id,
                    dragon.name,
                    dragon.creation_date,
                    dragon.age,
                    dragon.weight,
                    dragon.speaking,
                    dragon.dragon_character,
                    coordinate.x,
                    coordinate.y,
                    person.name person_name,
                    person.height person_height,
                    person.weight person_weight,
                    location.x location_x,
                    location.y location_y,
                    location.z location_z,
                    location.name location_name
                    from dragon\s
                    left join coordinate on dragon.id = coordinate.dragon_id\s
                    left join person on dragon.id = person.dragon_id\s
                    left join location on person.dragon_id = location.person_id""";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                Hashtable<Long, Dragon> dragonHashtable = new Hashtable<>();
                while (rs.next()) {
                    Integer userId = rs.getInt("user_id");
                    long id = rs.getLong("id");
                    String name = rs.getString("name");
                    Date creationDate = rs.getDate("creation_date");
                    Long age = rs.getLong("age");
                    double weight = rs.getDouble("weight");
                    Boolean speaking = rs.getBoolean("speaking");
                    Integer character = rs.getInt("dragon_character");
                    Double x = rs.getDouble("x");
                    Float y = rs.getFloat("y");
                    String personName = rs.getString("person_name");
                    Double height = rs.getDouble("person_height");
                    Long weightPerson = rs.getLong("person_weight");
                    Double xLocation = rs.getDouble("location_x");
                    Double yLocation = rs.getDouble("location_y");
                    Float zLocation = rs.getFloat("location_z");
                    String nameLocation = rs.getString("location_name");

                    Dragon dragon = new Dragon();
                    dragon.setUserId(userId);
                    dragon.setId(id);
                    dragon.setName(name);
                    dragon.setCreationDate(creationDate.toInstant().atZone(ZoneId.systemDefault()));
                    dragon.setAge(age);
                    dragon.setWeight(weight);
                    dragon.setSpeaking(speaking);
                    dragon.setCharacter(DragonCharacterHelper.parseDragonCharacter(character));
                    dragon.setCoordinates(new Coordinates(x, y));
                    dragon.setKiller(new Person(personName, height, weightPerson, new Location(xLocation, yLocation, zLocation, nameLocation)));
                    dragonHashtable.put(id, dragon);
                }
                return dragonHashtable;
            } catch (SQLException e) {
                throw new Error("Problem", e);
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public Hashtable<Long, Dragon> saveDragons(Hashtable<Long, Dragon> dragonHashtable, Integer userId) {
        // I delete all dragons that doesn't belong to the  current user
        dragonHashtable.entrySet().removeIf(entry -> !entry.getValue().getUserId().equals(userId));

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            String query = "Delete from public.dragon where user_id = " + userId;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(query);
                Statement stmtInsert = conn.createStatement();
                dragonHashtable.forEach((k, v) -> {
                    String insert = v.toInsert();
                    try {
                        stmtInsert.execute(insert);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                return this.getDragons();

            } catch (SQLException e) {
                throw new Error("Problem", e);
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public boolean IsValidUser(User userToValidate) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            Statement stmt;
            String query = "Select password from public.users where name = '"
                    + userToValidate.getName() + "';";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next())
                return false;

            byte[] dataToValidate = rs.getBytes("password");
            return Arrays.equals(dataToValidate, new EncriptionHelper().encript(userToValidate.getPassword()));
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void CreateAdmin() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);

            byte[] encryptedData = new EncriptionHelper().encript("12345");
            String query = "Insert into public.users (name, password) values ('Francisco', ?);";

            PreparedStatement pst = conn.prepareStatement(query);
            pst.setBytes(1, encryptedData);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}