package com.itmo.dragon.server;
import com.itmo.dragon.shared.entities.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Hashtable;

public class DragonsReader {

    public Hashtable<Long, Dragon> read(String data) {
        data = removeHeader(data);
        if (data == null)
            return null;
        return getDragons(data, "dragons");
    }

    private Hashtable<Long, Dragon> getDragons(String data, String tagName) {
        StringBuilder bodyBox = new StringBuilder();
        StringBuilder dataBox = new StringBuilder();
        dataBox.append(data);
        bodyBox.append(getBody(dataBox, tagName));
        if (bodyBox.length() == 0)
            return new Hashtable<>();
        Hashtable<Long, Dragon> dragons = new Hashtable<>();
        while (bodyBox.length() > 0) {
            Dragon dragon = getDragon(bodyBox, "dragon");
            if (dragon != null)
                dragons.put(dragon.getId(), dragon);
        }
        return dragons;
    }

    private Dragon getDragon(StringBuilder data, String tagName) {
        StringBuilder bodyBox = new StringBuilder();
        bodyBox.append(getBody(data, tagName));
        if (bodyBox.length() == 0)
            return null;
        Dragon dragon = new Dragon();
        dragon.setUserId(getInteger(bodyBox, "userId"));
        dragon.setId(getLong(bodyBox, "id"));
        dragon.setName(getString(bodyBox, "name"));
        dragon.setAge(getLong(bodyBox, "age"));
        dragon.setWeight(getDouble(bodyBox, "weight"));
        dragon.setCreationDate(getZonedDateTime(bodyBox, "creationDate"));
        dragon.setSpeaking(getBoolean(bodyBox, "speaking"));
        dragon.setCharacter(getCharacter(bodyBox, "character"));
        dragon.setCoordinates(getCoordinates(bodyBox, "coordinates"));
        dragon.setKiller(getKiller(bodyBox, "killer"));
        return dragon;
    }

    private Person getKiller(StringBuilder data, String tagName) {
        StringBuilder bodyBox = new StringBuilder();
        bodyBox.append(getBody(data, tagName));
        if (bodyBox.length() == 0)
            return null;
        Person killer = new Person();
        killer.setName(getString(bodyBox, "name"));
        killer.setHeight(getDouble(bodyBox, "height"));
        killer.setWeight(getLong(bodyBox, "weight"));
        killer.setLocation(getLocation(bodyBox, "location"));
        return killer;
    }

    private Location getLocation(StringBuilder data, String tagName) {
        StringBuilder bodyBox = new StringBuilder();
        bodyBox.append(getBody(data, tagName));
        if (bodyBox.length() == 0)
            return null;
        Location location = new Location();
        location.setName(getString(bodyBox, "name"));
        location.setX(getDouble(bodyBox, "x"));
        location.setY(getDouble(bodyBox, "y"));
        location.setZ(getFloat(bodyBox, "z"));
        return location;
    }

    private DragonCharacter getCharacter(StringBuilder data, String tagName) {
        String body = getBody(data, tagName);
        return DragonCharacterHelper.parseDragonCharacter(body);
    }


    private Coordinates getCoordinates(StringBuilder data, String tagName) {
        StringBuilder bodyBox = new StringBuilder();
        bodyBox.append(getBody(data, tagName));
        if (bodyBox.length() == 0)
            return null;
        Coordinates coordinates = new Coordinates();
        coordinates.setX(getDouble(bodyBox, "x"));
        coordinates.setY(getFloat(bodyBox, "y"));
        return coordinates;
    }

    private float getFloat(StringBuilder data, String tagName) {
        String body = getBody(data, tagName);
        return Float.parseFloat(body);
    }

    private ZonedDateTime getZonedDateTime(StringBuilder data, String tagName) {
        String body = getBody(data, tagName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(body).toInstant().atZone(ZoneId.systemDefault());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Boolean getBoolean(StringBuilder data, String tagName) {
        String body = getBody(data, tagName);
        return Boolean.parseBoolean(body);
    }

    private double getDouble(StringBuilder data, String tagName) {
        String body = getBody(data, tagName);
        return Double.parseDouble(body);
    }

    private Integer getInteger(StringBuilder data, String tagName) {
        String body = getBody(data, tagName);
        return Integer.parseInt(body);
    }

    private Long getLong(StringBuilder data, String tagName) {
        String body = getBody(data, tagName);
        return Long.parseLong(body);
    }

    private String getString(StringBuilder data, String tagName) {
        return getBody(data, tagName);
    }

    private String getBody(StringBuilder data, String tagName) {
        String openTag = String.format("<%s>", tagName);
        Integer openTagIndex = data.indexOf(openTag + "\n");
        Integer endOpenTagIndex = 0;
        if (openTagIndex < 0) {
            openTagIndex = data.indexOf(openTag);
            if (openTagIndex < 0)
                return null;
            endOpenTagIndex = openTagIndex + openTag.length();
        } else {
            endOpenTagIndex = openTagIndex + openTag.length() + "\n".length();
        }

        String closeTag = String.format("</%s>", tagName);
        Integer closeTagIndex = data.indexOf(closeTag + "\n");
        Integer endCloseTagIndex;
        if (closeTagIndex < 0) {
            closeTagIndex = data.indexOf(closeTag);
            if (closeTagIndex < 0)
                return null;
            endCloseTagIndex = closeTagIndex + closeTag.length();
        } else {
            endCloseTagIndex = closeTagIndex + closeTag.length() + "\n".length();
        }
        String body = data.substring(endOpenTagIndex, closeTagIndex);
        String aux = data.substring(endCloseTagIndex);
        data.delete(0, data.length());
        data.append(aux);
        return body;
    }

    private String removeHeader(String data) {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
        if (data.indexOf(header + "\n") >= 0)
            return data.substring((header + "\n").length());
        if (data.indexOf(header) >= 0)
            return data.substring(header.length());
        return null;
    }
}