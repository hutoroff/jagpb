package ru.hutoroff.jagpb.bot.commands;

import org.apache.commons.cli.CommandLine;
import ru.hutoroff.jagpb.bot.exceptions.UnknownCommandException;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;

public class CommandHelpCommand extends Command {

	private final CommandType commandTypeToHelp;

	public CommandHelpCommand(String command) throws UnknownOptionsException, UnknownCommandException {
		super(CommandType.COMMAND_HELP, null);
		this.commandTypeToHelp = findCommandToHelp(command);
	}

	private CommandType findCommandToHelp(String command) throws UnknownCommandException {
		String resCommand = command.trim();
		int indexOfBotName = resCommand.indexOf('@');
		if (indexOfBotName >= 0) {
			resCommand = resCommand.substring(0, indexOfBotName);
		}

		return CommandType.getByCommand(resCommand);
	}

	@Override
	protected CommandLine initArguments(String[] split) throws UnknownOptionsException {
		return null;
	}

	public String getRequestedHelp() {
		switch (this.commandTypeToHelp) {
			case START:
				return Help.START_COMMAND;
			case COMMAND_HELP:
				return Help.COMMAND_HELP_COMMAND;
			case CREATE_POLL:
				return Help.CREATE_POLL_COMMAND;
		}
		return "Unknown command";
	}

	@Override
	public String getHelp() {
		return Help.COMMAND_HELP_COMMAND;
	}
}
