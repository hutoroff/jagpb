package ru.hutoroff.jagpb.bot.commands.implementation;

import org.apache.commons.cli.*;
import ru.hutoroff.jagpb.bot.commands.Command;
import ru.hutoroff.jagpb.bot.commands.CommandType;
import ru.hutoroff.jagpb.bot.commands.Help;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LastPollResultCommand extends Command {
	private static final Options options = new Options();
	private static final CommandLineParser cliParser = new DefaultParser();
	private static final String HELP;
	private static final String COMMAND_SYNTAX = "/lastpollresult [-c CHAT_ID]";

	static {
		options.addOption(Option.builder("c")
				.longOpt("chat")
				.hasArg()
				.optionalArg(false)
				.argName("CHAT_ID")
				.desc("id of chat to get last poll for")
				.numberOfArgs(1)
				.required(false)
				.build()
		);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(pw, HelpFormatter.DEFAULT_WIDTH, COMMAND_SYNTAX, null, options, 3, 0, null);
		HELP = sw.toString();
	}

	public LastPollResultCommand(String[] arguments) throws UnknownOptionsException {
		super(CommandType.LAST_POLL_RESULT, arguments);
	}

	@Override
	public String getHelp() {
		return Help.LAST_POLL;
	}

	@Override
	protected CommandLine initArguments(String[] split) throws UnknownOptionsException {
		try {
			if (split == null || split.length == 0) {
				return cliParser.parse(options, new String[0]);
			}
			return cliParser.parse(options, split);
		} catch (ParseException e) {
			throw new UnknownOptionsException(HELP, e);
		}
	}
}
