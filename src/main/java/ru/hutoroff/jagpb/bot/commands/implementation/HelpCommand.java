package ru.hutoroff.jagpb.bot.commands.implementation;

import com.google.common.collect.Lists;
import org.apache.commons.cli.CommandLine;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import ru.hutoroff.jagpb.bot.commands.Command;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;
import ru.hutoroff.jagpb.business.PollService;

import java.util.List;

public class HelpCommand extends Command {
	private static final String MESSAGE = "To create new poll use command /create";

	private final Long chatId;

	public HelpCommand(Long chatId, PollService pollService) throws UnknownOptionsException {
		super( null, pollService);
		this.chatId = chatId;
	}

	@Override
	public List<BotApiMethod> execute() {
		return Lists.newArrayList(prepareSimpleReply(chatId, MESSAGE));
	}

	@Override
	protected CommandLine initArguments(String[] split) {
		return null;
	}
}
