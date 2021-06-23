package com.itmo.dragon.shared.entities;

import java.io.Serializable;

/**
 * Person class
 *
 * @author: Francisco Estrella
 * @version: 0.1
 */
public class Person implements Comparable<Long>, Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Double height; //Поле не может быть null, Значение поля должно быть больше 0
    private long weight; //Значение поля должно быть больше 0
    private Location location; //Поле не может быть null

    public Person() {
    }
    public Person(String personName, Double height, Long weightPerson, Location location) {
        setName(personName);
        setHeight(height);
        setWeight(weightPerson);
        setLocation(location);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String toXml() {
        String nameXml = String.format("<name>%s</name>", getName());
        String heightXml = String.format("<height>%s</height>", getHeight());
        String weightXml = String.format("<weight>%s</weight>", getWeight());
        String locationXml = getLocation().toXml();
        return String.format("<killer>%s%s%s%s</killer>", nameXml, heightXml, weightXml, locationXml);
    }

    @Override
    public int compareTo(Long o) {
        return (int) (this.getWeight() - o);
    }
}
