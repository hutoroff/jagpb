package ru.hutoroff.jagpb.bot.commands;

import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Service;
import ru.hutoroff.jagpb.bot.exceptions.UnknownCommandException;

import java.util.Arrays;

@Service
public class CommandBuilder {

	public Command buildCommand(String command) throws ParseException, UnknownCommandException {
		String[] split = command.split(" ");
		CommandType commandType = CommandType.getByCommand(split[0]);

		switch (commandType) {
			case START:
				return new StartCommand(commandType, null);
			case CREATE_POLL:
				return new CreatePollCommand(commandType, Arrays.copyOfRange(split, 1, split.length));
		}
		throw new IllegalArgumentException("Command '" + command + "' can not be executed");
	}
}
