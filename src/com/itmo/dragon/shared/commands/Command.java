package com.itmo.dragon.shared.commands;

import com.itmo.dragon.shared.entities.User;

import java.io.Serializable;

public class Command implements Serializable {
    private User user;
    private DataBox dataCommand;
    private CommandType commandType;

    public Command() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DataBox getDataCommand() {
        return dataCommand;
    }

    public void setDataCommand(DataBox dataCommand) {
        this.dataCommand = dataCommand;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
}
