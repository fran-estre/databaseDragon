package com.itmo.dragon.client;

import com.itmo.dragon.shared.entities.*;

import java.util.Scanner;

public class DragonReader {
    public Dragon read() {
        Scanner keyboard = new Scanner(System.in);
        String name;
        do {
            System.out.println("Enter dragon name: ");
            name = keyboard.nextLine();
            if (name.isEmpty()) {
                System.out.println("The name is invalid.");
            }
        } while (name.isEmpty());

        long age;
        do {
            System.out.println("Enter dragon age: ");
            age = readLong(keyboard, "Enter the correct value for the age.");
            if (age == 0) {
                System.out.println("The age is invalid.");
            }
        } while (age <= 0);

        double weight;
        do {
            System.out.println("Enter weight: ");
            weight = readDouble(keyboard, "Enter the correct value for the weight.");
            if (weight <= 0) {
                System.out.println("The weight is invalid.");
            }
        } while (weight <= 0);

        boolean speaking = false;
        System.out.println("Enter speaking (y: yes, otherwise no): ");
        String value = keyboard.nextLine();
        if (value.equalsIgnoreCase("Y")) {
            speaking = true;
        }

        Coordinates coordinates = createCoordinates(keyboard);
        DragonCharacter character = createDragonCharacter(keyboard);
        Person killer = createPerson(keyboard);
        return new Dragon(name, coordinates, age, weight, speaking, character, killer);
    }

    public static Double readDouble(Scanner keyboard, String message) {
        while (true) {
            String value = keyboard.nextLine();
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.out.println(message);
            }
        }
    }

    public static Long readLong(Scanner keyboard, String message) {
        while (true) {
            String value = keyboard.nextLine();
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                System.out.println(message);
            }
        }
    }

    private static Coordinates createCoordinates(Scanner keyboard) {
        System.out.println("Enter coordinate x: ");
        Double x = readDouble(keyboard, "Enter the correct value for the coordinate.");

        System.out.println("Enter coordinate y: ");
        Float y = readFloat(keyboard, "Enter the correct value for the coordinate.");

        Coordinates coordinates = new Coordinates();
        coordinates.setX(x);
        coordinates.setY(y);
        return coordinates;
    }

    private static DragonCharacter createDragonCharacter(Scanner keyboard) {
        String value;
        DragonCharacter character = null;
        do {
            System.out.println("Enter dragon character (EVIL, GOOD, CHAOTIC, FICKLE): ");
            value = keyboard.nextLine().toUpperCase();
            switch (value) {
                case "EVIL" -> character = DragonCharacter.EVIL;
                case "GOOD" -> character = DragonCharacter.GOOD;
                case "CHAOTIC" -> character = DragonCharacter.CHAOTIC;
                case "FICKLE" -> character = DragonCharacter.FICKLE;
                default -> System.out.println("The dragon character is invalid.");
            }
        } while (character == null);
        return character;
    }

    private static Person createPerson(Scanner keyboard) {
        String personName;
        do {
            System.out.println("Enter person name: ");
            personName = keyboard.nextLine();
            if (personName.isEmpty()) {
                System.out.println("The person name is invalid.");
            }
        } while (personName.isEmpty());

        long personWeight;
        do {
            System.out.println("Enter person weight: ");
            personWeight = readLong(keyboard, "Enter the correct value for the weight.");
            if (personWeight <= 0) {
                System.out.println("The person weight is invalid.");
            }
        } while (personWeight <= 0);

        double height;
        do {
            System.out.println("Enter height: ");
            height = readDouble(keyboard, "Enter the correct value for the height.");
            if (height <= 0) {
                System.out.println("The height is invalid.");
            }
        } while (height <= 0);


        Location location = createLocation(keyboard);

        Person killer = new Person();
        killer.setHeight(height);
        killer.setName(personName);
        killer.setWeight(personWeight);
        killer.setLocation(location);
        return killer;
    }

    public static Float readFloat(Scanner keyboard, String message) {
        while (true) {
            String value = keyboard.nextLine();
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                System.out.println(message);
            }
        }
    }

    private static Location createLocation(Scanner keyboard) {
        String name;
        do {
            System.out.println("Enter location name: ");
            name = keyboard.nextLine();
            if (name.isEmpty()) {
                System.out.println("The location name is invalid.");
            }
        } while (name.isEmpty());

        System.out.println("Enter location x: ");
        Double x = readDouble(keyboard, "Enter the correct value for the coordinate.");

        System.out.println("Enter location y: ");
        Double y = readDouble(keyboard, "Enter the correct value for the coordinate.");

        System.out.println("Enter coordinate z: ");
        Float z = readFloat(keyboard, "Enter the correct value for the coordinate.");

        Location location = new Location();
        location.setName(name);
        location.setX(x);
        location.setY(y);
        location.setZ(z);
        return location;
    }
}