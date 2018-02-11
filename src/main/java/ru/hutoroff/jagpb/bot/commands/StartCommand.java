package ru.hutoroff.jagpb.bot.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class StartCommand extends Command {

	public StartCommand(CommandType type, String[] arguments) throws ParseException {
		super(type, arguments);
	}

	@Override
	protected CommandLine initArguments(String[] split) throws ParseException {
		return null;
	}
}
