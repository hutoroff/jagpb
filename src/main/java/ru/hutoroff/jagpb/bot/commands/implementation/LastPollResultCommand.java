package ru.hutoroff.jagpb.bot.commands.implementation;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.hutoroff.jagpb.bot.commands.Command;
import ru.hutoroff.jagpb.bot.commands.Help;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;
import ru.hutoroff.jagpb.bot.messages.PollInfoBuilder;
import ru.hutoroff.jagpb.business.PollService;
import ru.hutoroff.jagpb.data.model.PollDO;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class LastPollResultCommand extends Command {
	private static final Options options = new Options();
	private static final CommandLineParser cliParser = new DefaultParser();
	private static final String HELP;
	private static final String COMMAND_SYNTAX = Help.LAST_POLL;
	private static final String NO_POLLS_MESSAGE = "No polls from current user in chat";

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

		options.addOption(Option.builder("r")
				.longOpt("removeCommand")
				.hasArg(false)
				.desc("after execution command will be deleted form chat")
				.required(false)
				.build()
		);

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(pw, HelpFormatter.DEFAULT_WIDTH, COMMAND_SYNTAX, null, options, 3, 0, null);
		HELP = sw.toString();
	}

	private final Message message;

	public LastPollResultCommand(String[] arguments, Message message, PollService pollService) throws UnknownOptionsException {
		super(arguments, pollService);
		this.message = message;
	}

	@Override
	public List<BotApiMethod> execute() {
		final Long chatId = message.getChatId();
		final List<BotApiMethod> result = new ArrayList<>();

		String chatIdCmd = arguments.getOptionValue('c');
		Long chatIdActual = (chatIdCmd != null && StringUtils.isNumeric(chatIdCmd.replaceAll("-", ""))) ? Long.valueOf(chatIdCmd) : chatId;
		PollDO pollToReport = pollService.getLastPollForUserInChat(message.getFrom().getId(), chatIdActual);
		if (pollToReport != null) {
			result.add(prepareSimpleReply(chatId, PollInfoBuilder.preparePollingReport(pollToReport)));
		} else {
			result.add(prepareSimpleReply(chatId, NO_POLLS_MESSAGE));
		}

		if (arguments.hasOption("r")) {
			DeleteMessage deleteMessage = prepareDeleteMessage(message);
			if (deleteMessage != null) {
				result.add(deleteMessage);
			}
		}

		return result;
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
