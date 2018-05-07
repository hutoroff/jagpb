package ru.hutoroff.jagpb.bot.commands;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.hutoroff.jagpb.bot.exceptions.UnknownOptionsException;
import ru.hutoroff.jagpb.business.PollService;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class Command implements Callable<List<BotApiMethod>> {
    protected static final Logger LOG = LoggerFactory.getLogger(Command.class);

    protected final CommandLine arguments;
    protected final PollService pollService;

    public Command(String[] arguments, PollService pollService) throws UnknownOptionsException {
        this.arguments = initArguments(arguments);
        this.pollService = pollService;
    }

    public abstract List<BotApiMethod> execute();

    protected abstract CommandLine initArguments(String[] split) throws UnknownOptionsException;

    @Override
    public final List<BotApiMethod> call() {
        return execute();
    }

    protected final SendMessage prepareSimpleReply(Long chatId, String replyText) {
        SendMessage reply = new SendMessage(chatId, replyText);
        reply.setParseMode(ParseMode.HTML);
        return reply;
    }

    protected final DeleteMessage prepareDeleteMessage(Message msg) {
        if (msg.getChat().isUserChat()) {
            LOG.debug("Not allowed to delete message from user chat");
            return null;
        }

        return new DeleteMessage(msg.getChatId(), msg.getMessageId());
    }
}
