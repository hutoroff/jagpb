package ru.hutoroff.jagpb.bot.commands;

import org.springframework.stereotype.Service;
import ru.hutoroff.jagpb.bot.commands.implementation.*;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommandBuilder {
	private static final Pattern pattern = Pattern.compile(" -[a-z]");

	public Command buildCommand(String command) throws UnknownOptionsException {
		String[] split = splitCommand(command);
		CommandType commandType = CommandType.getByCommand(split[0]);

		if (commandType == null) {
			return null;
		}

		switch (commandType) {
			case COMMAND_HELP:
				if (split.length == 1) {
					return new CommandHelpCommand(split[0]);
				}
				return new CommandHelpCommand(split[1]);
			case CREATE_POLL:
				if (CreatePollCommand.REQUIRED_OPTIONS_NUMBER > (split.length -1)) {
					return new CommandHelpCommand(split[0]);
				}
				return new CreatePollCommand(Arrays.copyOfRange(split, 1, split.length));
			case HELP:
				return new HelpCommand();
			case LAST_POLL_RESULT:
				if (split.length == 1) {
					return new LastPollResultCommand(null);
				} else {
					return new LastPollResultCommand(Arrays.copyOfRange(split, 1, split.length));
				}
			case START:
				return new StartCommand();
		}
		throw new IllegalArgumentException("Command '" + command + "' can not be executed");
	}

	private String[] splitCommand(String command) {
		final Integer[] argumentStarts = getArgumentStarts(command);
		List<String> result = new ArrayList<>();
		if (argumentStarts.length > 0) {
			result.add(command.substring(0, argumentStarts[0] - 1));
		} else {
			return command.split(" ");
		}

		for (int i = 0; i < argumentStarts.length; i++) {
			final int cmdStart = argumentStarts[i];
			final int cmdEnd = argumentStarts.length == (i + 1) ?
					command.length() : argumentStarts[i+1];

			String cmd = command.substring(cmdStart, cmdEnd).trim();
			final int indexOfOptionEnd = cmd.indexOf(' ');
			result.add(cmd.substring(0, indexOfOptionEnd != -1 ? indexOfOptionEnd : cmd.length()));
			cmd = cmd.substring(indexOfOptionEnd != -1 ? indexOfOptionEnd + 1 : 0).trim();
			result.add(cmd.substring(1, cmd.length() - 1));
		}

		return result.toArray(new String[0]);
	}

	private Integer[] getArgumentStarts(String command) {
		List<Integer> result = new ArrayList<>();
		final Matcher matcher = pattern.matcher(command);

		while (matcher.find()) {
			result.add(matcher.start() + 1);
		}

		return result.toArray(new Integer[0]);
	}
}
