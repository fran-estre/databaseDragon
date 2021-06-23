package com.itmo.dragon.shared.commands;

import java.io.Serializable;

public enum CommandType implements Serializable {
    HELP,
    INFO,
    SHOW,
    CLEAR,
    PRINT_CHARACTER,
    REMOVE_KEY,
    REPLACE_IF_GREATER,
    REPLACE_IF_LOWER,
    REMOVE_GREATER_KEY,
    COUNT_BY_CHARACTER,
    FILTER_LESS_THAN_KILLER,
    INSERT,
    UPDATE,
    EXECUTE_SCRIPT
}
