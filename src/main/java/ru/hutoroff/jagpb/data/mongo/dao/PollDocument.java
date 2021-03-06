package ru.hutoroff.jagpb.data.mongo.dao;

public interface PollDocument extends MongoDocument {
    String F_AUTHOR = "author";
    String F_CHAT = "chat";
    String F_TITLE = "title";
    String F_CREATED = "created";
    String F_OPTIONS = "options";
    String F_OPTION_TITLE = "title";
    String F_OPTION_ID = "id";
    String F_OPTION_VOTERS = "voters";
    String F_OPTION_VOTER_ID = "id";
    String F_OPTION_VOTER_DATE = "date";
    String F_OPTION_VOTER_USERNAME = "username";
    String F_OPTION_VOTER_FIRSTNAME = "firstname";
    String F_OPTION_VOTER_LASTNAME = "lastname";
}
