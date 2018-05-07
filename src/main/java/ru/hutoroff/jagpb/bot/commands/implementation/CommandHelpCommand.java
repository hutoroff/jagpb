package ru.hutoroff.jagpb.bot.commands.implementation;

import com.google.common.collect.Lists;
import org.apache.commons.cli.CommandLine;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import ru.hutoroff.jagpb.bot.commands.Command;
import ru.hutoroff.jagpb.bot.commands.CommandType;
import ru.hutoroff.jagpb.bot.commands.Help;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;
import ru.hutoroff.jagpb.business.PollService;

import java.util.List;

public class CommandHelpCommand extends Command {
	private static final String MSG_PREFIX = "usage: ";

	private final CommandType commandTypeToHelp;
	private final Long chatId;

	public CommandHelpCommand(String command, Long chatId, PollService pollService) throws UnknownOptionsException {
		super(null, pollService);
		this.commandTypeToHelp = findCommandToHelp(command);
		this.chatId = chatId;
	}

	@Override
	public List<BotApiMethod> execute() {
		SendMessage sendMessage = super.prepareSimpleReply(chatId, getRequestedHelp());
		return Lists.newArrayList(sendMessage);
	}

	@Override
	protected CommandLine initArguments(String[] split) {
		return null;
	}

	private CommandType findCommandToHelp(String command) {
		String resCommand = command.trim();
		int indexOfBotName = resCommand.indexOf('@');
		if (indexOfBotName >= 0) {
			resCommand = resCommand.substring(0, indexOfBotName);
		}

		return CommandType.getByCommand(resCommand);
	}

	private String getRequestedHelp() {
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
}
