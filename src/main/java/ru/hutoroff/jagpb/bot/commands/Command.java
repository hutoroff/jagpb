package ru.hutoroff.jagpb.bot.commands;

import org.apache.commons.cli.CommandLine;
import ru.hutoroff.jagpb.bot.exceptions.UnknownCommandException;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;

public abstract class Command {
    
    private final CommandType type;
    private final CommandLine arguments;

    public Command(CommandType type, String[] arguments) throws UnknownOptionsException {
        this.type = type;
        this.arguments = initArguments(arguments);
    }


    public Command(String rawCommand) throws UnknownCommandException, UnknownOptionsException {
        String[] split = rawCommand.split(" ");
        this.type = initType(split);

        if (split.length > 1) {
            this.arguments = initArguments(split);
        } else {
            this.arguments = null;
        }
    }

    public CommandType getType() {
        return type;
    }

    public CommandLine getArguments() {
        return arguments;
    }

    private CommandType initType(String[] split) throws UnknownCommandException {
        return CommandType.getByCommand(split[0]);
    }

    public abstract String getHelp();

    protected abstract CommandLine initArguments(String[] split) throws UnknownOptionsException;
}
