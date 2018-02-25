package ru.hutoroff.jagpb.bot;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.yaml.snakeyaml.Yaml;
import ru.hutoroff.jagpb.bot.commands.Command;
import ru.hutoroff.jagpb.bot.commands.CommandBuilder;
import ru.hutoroff.jagpb.bot.exceptions.UnknownCommandException;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;
import ru.hutoroff.jagpb.bot.messages.PollInfoBuilder;
import ru.hutoroff.jagpb.business.PollService;
import ru.hutoroff.jagpb.data.model.PollDO;
import ru.hutoroff.jagpb.data.model.PollOption;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component("pollingBot")
public class PollingBot extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(PollingBot.class);
    private static final String CONFIG_FILE_NAME = "pollingBotConfig.private.yml";

    private final PollService pollService;
    private final CommandBuilder commandBuilder;
    private PollingBotConfig configuration;

    @Autowired
    public PollingBot(CommandBuilder commandBuilder, PollService pollService) throws IOException {
        this.configuration = loadConfig();
        this.commandBuilder = commandBuilder;
        this.pollService = pollService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            final String messageText = update.getMessage().getText();
            LOG.trace("Received message: {}", messageText);

            if (messageText.startsWith("/")) {
                processCommand(update.getMessage());
            } else {
                doSimpleReply(update.getMessage().getChatId(), "Only commands can be processed");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            String[] split = callbackData.split("_");
            if(split.length != 3 && !split[0].equals("vote")) {
                LOG.error("Attempt to preform unknown callback action: {}", callbackData);
                return;
            }
            ObjectId pollId = new ObjectId(String.valueOf(split[1]));
            pollService.vote(pollId, split[2], update.getCallbackQuery().getFrom());

            PollDO poll = pollService.getPoll(pollId);
            EditMessageText editMessageText = new EditMessageText()
                    .setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                    .setText(PollInfoBuilder.prepareText(poll));
            editMessageText.setParseMode(ParseMode.MARKDOWN);
            editMessageText.setReplyMarkup(PollInfoBuilder.prepareKeybaord(poll.getOptions(), pollId.toString()));
            try {
                execute(editMessageText);
            } catch (TelegramApiException e) {
                LOG.error("Error on update message with poll result: ", e);
            }
        }
    }

    private void processCommand(Message message) {
        final String messageText = message.getText();
        Command command;
        try {
            command = commandBuilder.buildCommand(messageText);
        } catch (UnknownCommandException e) {
            LOG.warn("Unknown command: ", e);
            doSimpleReply(message.getChatId(), "Command is not supported");
            return;
        } catch (UnknownOptionsException e) {
            LOG.warn("Wrong arguments: ", e);
            doSimpleReply(message.getChatId(), e.getMessage());
            return;
        }

        executeCommand(command, message);
    }

    private void executeCommand(Command command, Message message) { //TODO: throw away and write again
        final Integer authorId = message.getFrom().getId();

        switch (command.getType()) {
            case START:
                doSimpleReply(message.getChatId(), "Welcome to " + configuration.getName() + "!");
                return;
            case CREATE_POLL:
                final String pollTitle = String.join(" ", command.getArguments().getOptionValues("t"));
                final String[] options = command.getArguments().getOptionValues("o");
                final List<PollOption> pollOptions = Arrays.stream(options).map(el -> new PollOption(StringUtils.strip(el, "\""))).collect(Collectors.toList());
                PollDO poll = pollService.createAndGetBackPoll(pollTitle, pollOptions, authorId);
                SendMessage sendMessage = PollInfoBuilder.buildPollMessage(poll, message.getChatId());
                sendMessage.setParseMode(ParseMode.MARKDOWN);

                sendReply(sendMessage);
                return;
            default:
                doSimpleReply(message.getChatId(), "No activity prepared for this command yet");
        }

    }

    @Override
    public String getBotUsername() {
        return configuration.getName();
    }

    @Override
    public String getBotToken() {
        return configuration.getToken();
    }

    private PollingBotConfig loadConfig() throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream is = ClassLoader.getSystemResourceAsStream(CONFIG_FILE_NAME)) {
            return yaml.loadAs(is, PollingBotConfig.class);
        }
    }

    private void doSimpleReply(Long chatId, String replyText) {
        SendMessage reply = new SendMessage(chatId, replyText);
        this.sendReply(reply);
    }

    private void sendReply(SendMessage msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            LOG.error("Error on sending reply:", e);
        }
    }
}
