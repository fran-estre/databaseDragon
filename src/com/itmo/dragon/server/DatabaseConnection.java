package com.itmo.dragon.server;

import com.itmo.dragon.shared.entities.*;

import java.sql.*;
import java.time.ZoneId;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class DatabaseConnection {

    public Hashtable<Long, Dragon> getDragons() {
        Connection conn = null;
        try {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres";

            conn = DriverManager.getConnection(url, user, password);

            Statement stmt = null;
            String query = "Select \n" +
                    "dragon.user_id,\n" +
                    "dragon.id,\n" +
                    "dragon.name,\n" +
                    "dragon.creation_date,\n" +
                    "dragon.age,\n" +
                    "dragon.weight,\n" +
                    "dragon.speaking,\n" +
                    "dragon.dragon_character,\n" +
                    "coordinate.x,\n" +
                    "coordinate.y,\n" +
                    "person.name person_name,\n" +
                    "person.height person_height,\n" +
                    "person.weight person_weight,\n" +
                    "location.x location_x,\n" +
                    "location.y location_y,\n" +
                    "location.z location_z,\n" +
                    "location.name location_name\n" +
                    "from dragon \n" +
                    "left join coordinate on dragon.id = coordinate.dragon_id \n" +
                    "left join person on dragon.id = person.dragon_id \n" +
                    "left join location on person.dragon_id = location.person_id";

            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                Hashtable<Long, Dragon> dragonHashtable = new Hashtable<>();
                while (rs.next()) {
                    Integer userId = rs.getInt("user_id");
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    Date creationDate = rs.getDate("creation_date");
                    Long age = rs.getLong("age");
                    Double weight = rs.getDouble("weight");
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
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
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
        //i delete all dragons that doesn't belong to the  current user
        Iterator<Map.Entry<Long, Dragon>> it = dragonHashtable.entrySet().iterator();
        int deleted = 0;
        while (it.hasNext()) {
            Map.Entry<Long, Dragon> entry = it.next();
            if (entry.getValue().getUserId() != userId) {
                it.remove();
                deleted++;
            }
        }

        Connection conn = null;
        try {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres";

            conn = DriverManager.getConnection(url, user, password);
            Statement stmt = null;
            String query = "Delete from public.dragon where user_id = " + userId;
            try {
                stmt = conn.createStatement();
                stmt.execute(query);
                Statement stmtInsert = conn.createStatement();
                dragonHashtable.forEach((k, v) -> {
                    String insert = v.toInsert();
                    try {
                        boolean execute = stmtInsert.execute(insert);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
                return this.getDragons();

            } catch (SQLException e) {
                throw new Error("Problem", e);
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
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
}