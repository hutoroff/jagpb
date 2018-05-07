package ru.hutoroff.jagpb.bot;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.hutoroff.jagpb.bot.commands.Command;
import ru.hutoroff.jagpb.bot.commands.CommandBuilder;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;
import ru.hutoroff.jagpb.bot.messages.PollInfoBuilder;
import ru.hutoroff.jagpb.business.PollService;
import ru.hutoroff.jagpb.configuration.ApplicationContextConfiguration;
import ru.hutoroff.jagpb.data.model.PollDO;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component("pollingBot")
public class PollingBot extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(PollingBot.class);

    private static final ExecutorService commandExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    private static final ExecutorService replySenderService = Executors.newSingleThreadExecutor();

    private final PollService pollService;
    private final CommandBuilder commandBuilder;
    private final PollingBotConfiguration botConfiguration;

    @Autowired
    public PollingBot(CommandBuilder commandBuilder, PollService pollService, ApplicationContextConfiguration applicationContextConfiguration) {
        this.commandBuilder = commandBuilder;
        this.pollService = pollService;
        this.botConfiguration = applicationContextConfiguration.botConfiguration();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            final String messageText = update.getMessage().getText();
            LOG.trace("Received message: {}", messageText);

            if (messageText.startsWith("/")) {
                processCommand(update.getMessage());
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            String[] split = callbackData.split("_");
            if(split.length != 3 && !split[0].equals("vote")) {
                LOG.error("Attempt to preform unknown callback action: {}", callbackData);
                return;
            }
            ObjectId pollId = new ObjectId(String.valueOf(split[1]));
            boolean voteResult = pollService.vote(pollId, split[2], update.getCallbackQuery().getFrom());

            if (voteResult) {
                updateMessage(pollId, update.getCallbackQuery());
            } else {
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
                answerCallbackQuery.setText("You've already voted for this option");
                doProcessAnswerCallback(answerCallbackQuery);
            }
        }
    }

    private void updateMessage(ObjectId pollId, CallbackQuery callbackQuery) {
        PollDO poll = pollService.getPoll(pollId);
        EditMessageText editMessageText = new EditMessageText()
                .setChatId(callbackQuery.getMessage().getChatId())
                .setMessageId(callbackQuery.getMessage().getMessageId())
                .setText(PollInfoBuilder.prepareText(poll));
        editMessageText.setParseMode(ParseMode.HTML);
        editMessageText.setReplyMarkup(PollInfoBuilder.prepareKeybaord(poll.getOptions(), pollId.toString()));

        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        try {
            execute(editMessageText);
            answerCallbackQuery.setText("You vote accepted");
        } catch (TelegramApiException e) {
            LOG.error("Error on update message with poll result: ", e);
            LOG.error("Caused by: ", e.getCause().toString());
            answerCallbackQuery.setText("Error while processing your vote!");
        }
        doProcessAnswerCallback(answerCallbackQuery);
    }

    private void doProcessAnswerCallback(AnswerCallbackQuery answerCallbackQuery) {
        try {
            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            LOG.warn("Error on answering on callback query: ", e);
        }
    }

    private void processCommand(Message message) {
        Command command;
        try {
            command = commandBuilder.buildCommand(message);
        } catch (UnknownOptionsException e) {
            LOG.warn("Wrong arguments: ", e);
            doSimpleReply(message.getChatId(), e.getMessage());
            return;
        }

        if (command == null) {
            LOG.debug("Unknown command: ", message.getText());
            return;
        }

        executeCommand(command);
    }

    private void executeCommand(Command command) {
        Future<List<BotApiMethod>> commandFuture = commandExecutorService.submit(command);
        ReplySender sender = new ReplySender(commandFuture);
        replySenderService.submit(sender);
    }

    @Override
    public String getBotUsername() {
        return botConfiguration.getName();
    }

    @Override
    public String getBotToken() {
        return botConfiguration.getToken();
    }

    private void doSimpleReply(Long chatId, String replyText) {
        SendMessage reply = new SendMessage(chatId, replyText);
        reply.setParseMode(ParseMode.HTML);
        this.processMethod(reply);
    }

    private synchronized void processMethod(BotApiMethod method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            LOG.error("Error processing method: {}. Caused by:", method, e);
        }
    }

    private class ReplySender implements Runnable {

        private final Future<List<BotApiMethod>> repliesFuture;

        ReplySender(Future<List<BotApiMethod>> repliesFuture) {
            this.repliesFuture = repliesFuture;
        }

        @Override
        public void run() {
            List<BotApiMethod> replies;
            try {
                replies = repliesFuture.get();
            } catch (InterruptedException e) {
                LOG.warn("Command processing was interrupted: ", e);
                Thread.currentThread().interrupt();
                return;
            } catch (ExecutionException e) {
                LOG.error("Error while executing command: ", e);
                return;
            }

            for (BotApiMethod reply : replies) {
                processMethod(reply);
            }
        }
    }
}
