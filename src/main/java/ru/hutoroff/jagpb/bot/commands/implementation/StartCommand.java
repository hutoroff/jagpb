package ru.hutoroff.jagpb.bot.commands.implementation;

import org.apache.commons.cli.CommandLine;
import ru.hutoroff.jagpb.bot.commands.Command;
import ru.hutoroff.jagpb.bot.commands.CommandType;
import ru.hutoroff.jagpb.bot.commands.Help;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;

public class StartCommand extends Command {

	public StartCommand() throws UnknownOptionsException {
		super(CommandType.START, null);
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
