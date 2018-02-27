package ru.hutoroff.jagpb.bot.commands;

import java.util.Arrays;
import java.util.Optional;

public enum CommandType {
    COMMAND_HELP("/commandhelp"),
    CREATE_POLL("/create"),
    HELP("/help"),
    LAST_POLL_RESULT("/lastpollresult"),
    START("/start");

    String commandText;

    CommandType(String commandText) {
        this.commandText = commandText;
    }

    public static CommandType getByCommand(String cmd) {
        Optional<CommandType> first = Arrays.stream(values()).filter(el -> cmd.startsWith(el.commandText)).findFirst();
        return first.orElse(null);
    }

    public String getCommandText() {
        return commandText;
    }
}
