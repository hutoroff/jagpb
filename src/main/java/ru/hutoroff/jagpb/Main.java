package ru.hutoroff.jagpb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import ru.hutoroff.jagpb.bot.PollingBot;
import ru.hutoroff.jagpb.configuration.ApplicationContextConfiguration;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static TelegramBotsApi botsApi;

    static {
        ApiContextInitializer.init();
        botsApi = new TelegramBotsApi();
    }

    public static void main(final String[] args) {
        LOG.info("Just Another Group Polling Bot - starting");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);
        LOG.info("Just Another Group Polling Bot - application context initialized");

        try {
            TelegramLongPollingBot pollingBot = ctx.getBean("pollingBot", PollingBot.class);
            botsApi.registerBot(pollingBot);
        } catch (TelegramApiRequestException e) {
            LOG.error("Error on attempt to register PollingBot:", e);
            return;
        }

        LOG.info("Just Another Group Polling Bot - started successfully");
    }
}
