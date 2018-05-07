package ru.hutoroff.jagpb.bot.commands.implementation;

import com.google.common.collect.Lists;
import org.apache.commons.cli.CommandLine;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import ru.hutoroff.jagpb.bot.commands.Command;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;
import ru.hutoroff.jagpb.business.PollService;

import java.util.List;

public class StartCommand extends Command {
	private static final String WELCOME_MESSAGE_FORMAT = "Welcome to the %s!";

	private final Long chatId;
	private final String botName;

	public StartCommand(PollService pollService, Long chatId, String botName) throws UnknownOptionsException {
		super(null, pollService);
		this.chatId = chatId;
		this.botName = botName;
	}

	@Override
	public List<BotApiMethod> execute() {
		return Lists.newArrayList(prepareSimpleReply(chatId, String.format(WELCOME_MESSAGE_FORMAT, botName)));
	}

	@Override
	protected CommandLine initArguments(String[] split) {
		return null;
	}
}
