package ru.hutoroff.jagpb.configuration;

import ru.hutoroff.jagpb.data.mongo.MongoConfiguration;

public class ApplicationConfiguration {
    private MongoConfiguration mongo;

    public MongoConfiguration getMongo() {
        return mongo;
    }

    public void setMongo(MongoConfiguration mongo) {
        this.mongo = mongo;
    }
}
