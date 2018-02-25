package ru.hutoroff.jagpb.bot.commands;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommandBuilderTest {
	private final static String TEST_VAL = "/create -t \"Poll Title\" -o {\"Option 1\",\"Option 2\"}";
	private final static CommandType COMMAND_TYPE = CommandType.CREATE_POLL;

	private CommandBuilder commandBuilder;

	@Before
	public void setUp() throws Exception {
		commandBuilder = new CommandBuilder();
	}

	@Test
	public void buildCommand() throws Exception {
		Command command = commandBuilder.buildCommand(TEST_VAL);
		Assert.assertEquals(COMMAND_TYPE, command.getType());
		Assert.assertEquals("Poll Title", command.getArguments().getOptionValue('t'));
		Assert.assertEquals("\"Option 1\"", command.getArguments().getOptionValues('o')[0]);
		Assert.assertEquals("\"Option 2\"", command.getArguments().getOptionValues('o')[1]);
	}

}