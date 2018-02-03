package ru.hutoroff.jagpb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import ru.hutoroff.jagpb.bot.PollingBot;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        LOG.info("Just Another Polling Bot - starting");

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();


        try {
            botsApi.registerBot(new PollingBot());
        } catch (TelegramApiRequestException e) {
            LOG.error("Error on attempt to register PollingBot:", e);
            return;
        } catch (IOException ie) {
            LOG.error("Error on reading bot config:", ie);
            return;
        }

        LOG.info("Just Another Polling Bot - started successfully");
    }
}
