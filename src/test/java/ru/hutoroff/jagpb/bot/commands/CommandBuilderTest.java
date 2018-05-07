package ru.hutoroff.jagpb.bot.commands;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.api.objects.Message;
import ru.hutoroff.jagpb.bot.PollingBotConfiguration;
import ru.hutoroff.jagpb.bot.commands.implementation.CreatePollCommand;
import ru.hutoroff.jagpb.business.PollService;

public class CommandBuilderTest {
	private final static String TEST_VAL = "/create -t \"Poll Title\" -o {\"Option 1\",\"Option 2\"}";

	private CommandBuilder commandBuilder;
	private PollService pollService;
	private PollingBotConfiguration botConfiguration;

	@Before
	public void setUp() {
		pollService = Mockito.mock(PollService.class);
		botConfiguration = Mockito.mock(PollingBotConfiguration.class);
		commandBuilder = new CommandBuilder(pollService, botConfiguration);
	}

	@Test
	public void buildCommand() throws Exception {
		Message messageMock = Mockito.mock(Message.class);
		Mockito.when(messageMock.getText()).thenReturn(TEST_VAL);
		Command command = commandBuilder.buildCommand(messageMock);
		Assert.assertEquals(CreatePollCommand.class, command.getClass());
		Assert.assertEquals("Poll Title", command.arguments.getOptionValue('t'));
		Assert.assertEquals("\"Option 1\"", command.arguments.getOptionValues('o')[0]);
		Assert.assertEquals("\"Option 2\"", command.arguments.getOptionValues('o')[1]);
	}

}