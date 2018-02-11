package ru.hutoroff.jagpb.data.mongo.dao;

public interface PollDocument extends MongoDocument {
    String F_AUTHOR = "author";
    String F_TITLE = "title";
    String F_OPTIONS = "options";
}
