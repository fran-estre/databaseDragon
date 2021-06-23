package com.itmo.dragon.shared.entities;

public class DragonCharacterHelper {
    public static DragonCharacter parseDragonCharacter(String character) {
        return switch (character) {
            case "EVIL" -> DragonCharacter.EVIL;
            case "GOOD" -> DragonCharacter.GOOD;
            case "CHAOTIC" -> DragonCharacter.CHAOTIC;
            case "FICKLE" -> DragonCharacter.FICKLE;
            default -> null;
        };
    }

    public static DragonCharacter parseDragonCharacter(Integer character) {
        return switch (character) {
            case 0 -> DragonCharacter.EVIL;
            case 1 -> DragonCharacter.GOOD;
            case 2 -> DragonCharacter.CHAOTIC;
            case 3 -> DragonCharacter.FICKLE;
            default -> null;
        };
    }

    public static Integer parseDragonCharacter(DragonCharacter character) {
        return switch (character) {
            case EVIL -> 0;
            case GOOD -> 1;
            case CHAOTIC -> 2;
            case FICKLE -> 3;
        };
    }

}
