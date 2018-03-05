package ru.hutoroff.jagpb.bot.commands.implementation;

import org.apache.commons.cli.CommandLine;
import ru.hutoroff.jagpb.bot.commands.Command;
import ru.hutoroff.jagpb.bot.commands.CommandType;
import ru.hutoroff.jagpb.bot.commands.Help;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;

public class CommandHelpCommand extends Command {
	private static final String MSG_PREFIX = "usage: ";

	private final CommandType commandTypeToHelp;

	public CommandHelpCommand(String command) throws UnknownOptionsException {
		super(CommandType.COMMAND_HELP, null);
		this.commandTypeToHelp = findCommandToHelp(command);
	}

	private CommandType findCommandToHelp(String command) {
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
		if (commandTypeToHelp != null) {
			switch (this.commandTypeToHelp) {
				case COMMAND_HELP:
					return MSG_PREFIX + Help.COMMAND_HELP_COMMAND;
				case CREATE_POLL:
					return MSG_PREFIX + Help.CREATE_POLL_COMMAND;
				case HELP:
					return MSG_PREFIX + Help.HELP;
				case LAST_POLL_RESULT:
					return MSG_PREFIX + Help.LAST_POLL;
				case START:
					return MSG_PREFIX + Help.START_COMMAND;
			}
		}
		return "Unknown command";
	}

	@Override
	public String getHelp() {
		return MSG_PREFIX + Help.COMMAND_HELP_COMMAND;
	}
}
