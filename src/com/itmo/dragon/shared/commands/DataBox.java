package com.itmo.dragon.shared.commands;

import com.itmo.dragon.shared.entities.Dragon;

import java.io.Serializable;

public class DataBox implements Serializable {
    Long weight;
    Long key;
    Long age;
    String dragonCharacter;
    String dataFile;
    Dragon dragon;
    String response;

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getDragonCharacter() {
        return dragonCharacter;
    }

    public void setDragonCharacter(String dragonCharacter) {
        this.dragonCharacter = dragonCharacter;
    }

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public void setDragon(Dragon dragon) {
        this.dragon = dragon;
    }

    public Dragon getDragon() {
        return dragon;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

