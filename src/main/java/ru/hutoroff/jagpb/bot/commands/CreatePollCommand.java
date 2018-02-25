package ru.hutoroff.jagpb.bot.commands;

import org.apache.commons.cli.*;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CreatePollCommand extends Command {
    private static final CommandLineParser cliParser = new DefaultParser();
    private static final HelpFormatter helpFormatter = new HelpFormatter();
    public static final int REQUIRED_OPTIONS_NUMBER = 2;
    private static final String COMMAND_SYNTAX = "/create -t \"Poll Title\" -o {\"option1\",\"option2\",...,\"optionN\"}";
    private static final String HELP;

    static {
        options.addOption(Option.builder("t")
                .longOpt("title")
                .optionalArg(false)
                .hasArg()
                .argName("TITLE")
                .desc("title of poll")
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .required()
                .build()
        );
        options.addOption(Option.builder("o")
                .longOpt("options")
                .optionalArg(false)
                .hasArg()
                .argName("OPTIONS")
                .desc("options to be added to poll")
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .valueSeparator(',')
                .required()
                .build()
        );

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        helpFormatter.printHelp(pw, HelpFormatter.DEFAULT_WIDTH, COMMAND_SYNTAX, null, options, 3,0,null);
        HELP = sw.toString();
    }

    public CreatePollCommand(CommandType type, String[] arguments) throws UnknownOptionsException {
        super(type, arguments);
    }

    @Override
    public String getHelp() {
        return HELP;
    }

    @Override
    protected CommandLine initArguments(String[] split) throws UnknownOptionsException {
        try {
            if (split.length == 0) {
                throw new UnknownOptionsException(HELP);
            }
            return cliParser.parse(options, split);
        } catch (ParseException e) {
            throw new UnknownOptionsException(HELP, e);
        }
    }
}
