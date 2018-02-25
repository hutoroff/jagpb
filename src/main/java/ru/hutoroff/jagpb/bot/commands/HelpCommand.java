package ru.hutoroff.jagpb.bot.commands;

import org.apache.commons.cli.CommandLine;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;

public class HelpCommand extends Command {

	public HelpCommand() throws UnknownOptionsException {
		super(CommandType.HELP, null);
	}

	@Override
	public String getHelp() {
		return Help.HELP;
	}

	@Override
	protected CommandLine initArguments(String[] split) throws UnknownOptionsException {
		return null;
	}
}
