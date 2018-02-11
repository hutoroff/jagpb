package ru.hutoroff.jagpb.bot.commands;

import org.apache.commons.cli.*;

public class CreatePollCommand extends Command {
    private final static CommandLineParser cliParser = new DefaultParser();
    private final static Options options = new Options();

    static {
        Option title = new Option("t", "title", true, "title of poll");
        title.setRequired(true);
        title.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(title);
    }

    public CreatePollCommand(CommandType type, String[] arguments) throws ParseException {
        super(type, arguments);
    }

    @Override
    protected CommandLine initArguments(String[] split) throws ParseException {
        return cliParser.parse(options, split);
    }
}
