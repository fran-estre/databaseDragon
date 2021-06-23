package com.itmo.dragon.shared.commands;

import com.itmo.dragon.client.DragonReader;
import com.itmo.dragon.client.FileReader;
import com.itmo.dragon.shared.entities.Dragon;

public class DataBoxHandler {
    public static boolean getDataBox(String[] parts, Command command, StringBuilder comments, boolean isInteractive) {
        DataBox dataBox;
        switch (parts[0]) {
            case "HELP" -> {
                command.setCommandType(CommandType.HELP);
                return true;
            }
            case "INFO" -> {
                command.setCommandType(CommandType.INFO);
                return true;
            }
            case "SHOW" -> {
                command.setCommandType(CommandType.SHOW);
                return true;
            }
            case "CLEAR" -> {
                command.setCommandType(CommandType.CLEAR);
                return true;
            }
            case "PRINT_CHARACTER" -> {
                command.setCommandType(CommandType.PRINT_CHARACTER);
                return true;
            }
            case "REMOVE_KEY" -> {
                command.setCommandType(CommandType.REMOVE_KEY);
                dataBox = readDataCommandKey(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "REPLACE_IF_GREATER" -> {
                command.setCommandType(CommandType.REPLACE_IF_GREATER);
                dataBox = readDataCommandKey(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "REPLACE_IF_LOWER" -> {
                command.setCommandType(CommandType.REPLACE_IF_LOWER);
                dataBox = readDataCommandKey(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "REMOVE_GREATER_KEY" -> {
                command.setCommandType(CommandType.REMOVE_GREATER_KEY);
                dataBox = readDataCommandKey(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "COUNT_BY_CHARACTER" -> {
                command.setCommandType(CommandType.COUNT_BY_CHARACTER);
                dataBox = readDataCommandCountByCharacter(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "FILTER_LESS_THAN_KILLER" -> {
                command.setCommandType(CommandType.FILTER_LESS_THAN_KILLER);
                dataBox = readDataCommandFilterLessThanKiller(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "INSERT" -> {
                if (!isInteractive)
                    return false;
                command.setCommandType(CommandType.INSERT);
                dataBox = readDataCommandInsert();
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "UPDATE" -> {
                if (!isInteractive)
                    return false;
                command.setCommandType(CommandType.UPDATE);
                dataBox = readDataCommandUpdate(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "EXECUTE_SCRIPT" -> {
                if (!isInteractive)
                    return false;
                command.setCommandType(CommandType.EXECUTE_SCRIPT);
                dataBox = readDataCommandExecuteScript(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            default -> {
                comments.append("unknown command");
                return false;
            }
        }
    }

    private static Dragon readDragon() {
        DragonReader dragonReader = new DragonReader();
        return dragonReader.read();
    }

    private static DataBox readDataCommandUpdate(String[] parts, StringBuilder comments) {
        if (parts.length < 2) {
            comments.append("The command is incomplete, you need to enter the key.");
            return null;
        }

        try {
            long key = Long.parseLong(parts[1]);
            Dragon dragonToUpdate = readDragon();
            dragonToUpdate.setId(key);
            DataBox dataBox = new DataBox();
            dataBox.setDragon(dragonToUpdate);
            return dataBox;
        } catch (NumberFormatException e) {
            comments.append("The command is invalid.");
            return null;
        }
    }

    private static DataBox readDataCommandInsert() {
        Dragon dragonToUpdate = readDragon();
        DataBox dataBox = new DataBox();
        dataBox.setDragon(dragonToUpdate);
        return dataBox;
    }

    private static DataBox readDataCommandFilterLessThanKiller(String[] parts, StringBuilder comments) {
        if (parts.length < 2) {
            comments.append("The command is incomplete, you need to enter the killer weight.");
            return null;
        }
        try {
            DataBox dataBox = new DataBox();
            dataBox.setWeight(Long.parseLong(parts[1]));
            return dataBox;
        } catch (NumberFormatException e) {
            comments.append("The command is invalid.");
            return null;
        }
    }

    private static DataBox readDataCommandCountByCharacter(String[] parts, StringBuilder comments) {
        if (parts.length < 2) {
            comments.append("The command is incomplete, you need to enter the character (EVIL, GOOD, CHAOTIC, FICKLE).");
            return null;
        }
        if (!parts[1].equals("EVIL") && !parts[1].equals("GOOD") && !parts[1].equals("CHAOTIC") && !parts[1].equals("FICKLE")) {
            comments.append("The command is invalid.");
            return null;
        }

        DataBox dataBox = new DataBox();
        dataBox.setDragonCharacter(parts[1]);
        return dataBox;
    }

    private static DataBox readDataCommandKey(String[] parts, StringBuilder comments) {
        if (parts.length < 3) {
            comments.append("The command is incomplete, you need to enter the key and the age.");
            return null;
        }

        DataBox dataBox = new DataBox();
        try {
            dataBox.setKey(Long.parseLong(parts[1]));
        } catch (NumberFormatException e) {
            comments.append("The command is invalid.");
            return null;
        }

        try {
            dataBox.setAge(Long.parseLong(parts[2]));
        } catch (NumberFormatException e) {
            comments.append("The command is invalid.");
            return null;
        }
        return dataBox;
    }

    private static DataBox readDataCommandExecuteScript(String[] parts, StringBuilder comments) {
        if (parts.length < 2) {
            comments.append("The command is incomplete, you need to enter the filename that contain the commands.");
            return null;
        }
        FileReader fileReader = new FileReader();
        String dataFile = fileReader.read(parts[1]);
        if (dataFile == null) {
            comments.append("Can't read the file.");
            return null;
        }
        DataBox dataBox = new DataBox();
        dataBox.setDataFile(dataFile);
        return dataBox;
    }
}
