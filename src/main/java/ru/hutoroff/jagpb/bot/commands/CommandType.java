package ru.hutoroff.jagpb.bot.commands;

import ru.hutoroff.jagpb.bot.exceptions.UnknownCommandException;

import java.util.Arrays;
import java.util.Optional;

public enum CommandType {
    START("/start"),
    CREATE_POLL("/create"),
    COMMAND_HELP("/commandHelp");

    String commandText;

    CommandType(String commandText) {
        this.commandText = commandText;
    }

    public static CommandType getByCommand(String cmd) throws UnknownCommandException {
        Optional<CommandType> first = Arrays.stream(values()).filter(el -> cmd.startsWith(el.commandText)).findFirst();
        if (first.isPresent()) {
            return first.get();
        }
        throw new UnknownCommandException("Command '" + cmd + "' is not registered");
    }

    public String getCommandText() {
        return commandText;
    }
}
