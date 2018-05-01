package ru.hutoroff.jagpb.configuration;

import ru.hutoroff.jagpb.bot.PollingBotConfiguration;
import ru.hutoroff.jagpb.data.mongo.MongoConfiguration;

public class ApplicationConfiguration {
    private MongoConfiguration mongo;
    private PollingBotConfiguration telegram;

    public MongoConfiguration getMongo() {
        return mongo;
    }

    public void setMongo(MongoConfiguration mongo) {
        this.mongo = mongo;
    }

    public PollingBotConfiguration getTelegram() {
        return telegram;
    }

    public void setTelegram(PollingBotConfiguration telegram) {
        this.telegram = telegram;
    }
}
