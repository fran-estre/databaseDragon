package com.itmo.dragon.shared.entities;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Dragon class
 *
 * @author: Francisco Estrella
 * @version: 0.1
 */
public class Dragon implements Serializable {
    private Integer userId;
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long age; //Значение поля должно быть больше 0, Поле не может быть null
    private double weight; //Значение поля должно быть больше 0
    private Boolean speaking; //Поле не может быть null
    private DragonCharacter character; //Поле может быть null
    private Person killer; //Поле может быть null

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Boolean getSpeaking() {
        return speaking;
    }

    public void setSpeaking(Boolean speaking) {
        this.speaking = speaking;
    }

    public DragonCharacter getCharacter() {
        return character;
    }

    public void setCharacter(DragonCharacter character) {
        this.character = character;
    }

    public Person getKiller() {
        return killer;
    }

    public void setKiller(Person killer) {
        this.killer = killer;
    }

    public Dragon() {
    }

    public Dragon(int userId, String name, Coordinates coordinates, Long age, Double weight, Boolean speaking, DragonCharacter character, Person killer) {
        setUserId(userId);
        setName(name);
        setCoordinates(coordinates);
        setAge(age);
        setWeight(weight);
        setSpeaking(speaking);
        setCharacter(character);
        setKiller(killer);
        setCreationDate(new Date().toInstant().atZone(ZoneId.systemDefault()));
    }

    public String toString() {
        return String.format("id: %s, name: %s", getId(), getName());
    }

    public String toXml() {
        String userIdXml = String.format("<userId>%s</userId>", getUserId());
        String idXml = String.format("<id>%s</id>", getId());
        String nameXml = String.format("<name>%s</name>", getName());
        String ageXml = String.format("<age>%s</age>", getAge());
        String weightXml = String.format("<weight>%s</weight>", getWeight());
        String creationDateXml = String.format("<creationDate>%s</creationDate>", getCreationDate());
        String speakingXml = String.format("<speaking>%s</speaking>", getSpeaking());
        String characterXml = String.format("<character>%s</character>", getCharacter());
        String coordinatesXml = getCoordinates().toXml();
        String killerXml = getKiller().toXml();
        return String.format("<dragon>%s%s%s%s%s%s%s%s%s%s</dragon>",
                userIdXml,
                idXml,
                nameXml,
                ageXml,
                weightXml,
                creationDateXml,
                speakingXml,
                characterXml,
                coordinatesXml,
                killerXml);
    }

    public String toInsert() {
        return "INSERT INTO public.dragon(name, creation_date, age, weight, speaking, dragon_character, user_id) " +
                "VALUES ('{0}', '{1}', {2}, {3}, {4}, {5}, {6});\n" +
                "INSERT INTO public.coordinate(dragon_id, x, y) " +
                "SELECT MAX(id), {7}, {8} FROM public.dragon;\n" +
                "INSERT INTO public.person(dragon_id, name, height, weight) " +
                "SELECT MAX(id), '{9}', {10}, {11} FROM public.dragon;\n" +
                "INSERT INTO public.location(person_id, x, y, z, name) " +
                "SELECT MAX(id), {12}, {13}, {14}, '{15}' FROM public.dragon;"
                        .replace("{0}", getName())
                        .replace("{1}", DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm").format(getCreationDate()))
                        .replace("{2}", getAge().toString())
                        .replace("{3}", String.valueOf(getWeight()))
                        .replace("{4}", getSpeaking().toString())
                        .replace("{5}", DragonCharacterHelper.parseDragonCharacter(getCharacter()).toString())
                        .replace("{6}", getUserId().toString())
                        .replace("{0}", getCoordinates().getX().toString())
                        .replace("{0}", String.valueOf(getCoordinates().getY()))
                        .replace("{0}", getKiller().getName())
                        .replace("{0}", getKiller().getHeight().toString())
                        .replace("{0}", String.valueOf(getKiller().getWeight()))
                        .replace("{0}", String.valueOf(getKiller().getLocation().getX()))
                        .replace("{0}", String.valueOf(getKiller().getLocation().getY()))
                        .replace("{0}", String.valueOf(getKiller().getLocation().getZ()))
                        .replace("{0}", getKiller().getLocation().getName());
    }
}