package ru.hutoroff.jagpb.bot.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ru.hutoroff.jagpb.bot.exceptions.UnknownCommandException;

public abstract class Command {
    private final CommandType type;
    private final CommandLine arguments;

    public Command(CommandType type, String[] arguments) throws ParseException {
        this.type = type;
        this.arguments = initArguments(arguments);
    }

    public Command(String rawCommand) throws ParseException, UnknownCommandException {
        String[] split = rawCommand.split(" ");
        this.type = initType(split);

        if (split.length > 1) {
            this.arguments = initArguments(split);
        } else {
            this.arguments = null;
        }
    }

    protected abstract CommandLine initArguments(String[] split) throws ParseException;

    private CommandType initType(String[] split) throws UnknownCommandException {
        return CommandType.getByCommand(split[0]);
    }

    public CommandType getType() {
        return type;
    }

    public CommandLine getArguments() {
        return arguments;
    }
}
