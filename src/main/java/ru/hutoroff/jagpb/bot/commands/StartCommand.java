package ru.hutoroff.jagpb.bot.commands;

import org.apache.commons.cli.CommandLine;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;

public class StartCommand extends Command {

	public StartCommand(CommandType type, String[] arguments) throws UnknownOptionsException {
		super(type, arguments);
	}

	@Override
	public String getHelp() {
		return Help.START_COMMAND;
	}

	@Override
	protected CommandLine initArguments(String[] split) throws UnknownOptionsException {
		return null;
	}
}
