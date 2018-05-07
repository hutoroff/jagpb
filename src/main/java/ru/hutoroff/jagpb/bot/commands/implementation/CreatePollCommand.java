package ru.hutoroff.jagpb.bot.commands.implementation;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.hutoroff.jagpb.bot.commands.Command;
import ru.hutoroff.jagpb.bot.commands.Help;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;
import ru.hutoroff.jagpb.bot.messages.PollInfoBuilder;
import ru.hutoroff.jagpb.business.PollService;
import ru.hutoroff.jagpb.data.model.PollDO;
import ru.hutoroff.jagpb.data.model.PollOption;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreatePollCommand extends Command {
    public static final int REQUIRED_OPTIONS_NUMBER = 2;
    private static final CommandLineParser cliParser = new DefaultParser();
    private static final String COMMAND_SYNTAX = Help.CREATE_POLL_COMMAND;
    private static final String HELP;
    private static final Options options = new Options();

    static {
        options.addOption(Option.builder("t")
                .longOpt("title")
                .optionalArg(false)
                .hasArg()
                .argName("TITLE")
                .desc("title of poll")
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .required()
                .build()
        );
        options.addOption(Option.builder("o")
                .longOpt("options")
                .optionalArg(false)
                .hasArg()
                .argName("OPTIONS")
                .desc("options to be added to poll")
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .valueSeparator(',')
                .required()
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
        helpFormatter.printHelp(pw, HelpFormatter.DEFAULT_WIDTH, COMMAND_SYNTAX, null, options, 3,0,null);
        HELP = sw.toString();
    }

    private final Message message;

    public CreatePollCommand(String[] arguments, Message message, PollService pollService) throws UnknownOptionsException {
        super(arguments, pollService);
        this.message = message;
    }

    @Override
    public List<BotApiMethod> execute() {
        final String pollTitle = String.join(" ", arguments.getOptionValues("t"));
        final String[] options = arguments.getOptionValues("o");
        final List<PollOption> pollOptions = Arrays.stream(options).map(el -> new PollOption(StringUtils.strip(el, "\""))).collect(Collectors.toList());
        final Long chatId = message.getChatId();
        final Integer authorId = message.getFrom().getId();

        PollDO createdPoll = pollService.createAndGetBackPoll(pollTitle, pollOptions, authorId, chatId);
        SendMessage sendMessage = PollInfoBuilder.buildPollMessage(createdPoll, chatId);
        sendMessage.setParseMode(ParseMode.HTML);

        List<BotApiMethod> result = new ArrayList<>();
        result.add(sendMessage);

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
            if (split.length == 0) {
                throw new UnknownOptionsException(HELP);
            }
            return cliParser.parse(options, split);
        } catch (ParseException e) {
            throw new UnknownOptionsException(HELP, e);
        }
    }
}
