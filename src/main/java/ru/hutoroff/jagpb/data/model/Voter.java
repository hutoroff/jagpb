package ru.hutoroff.jagpb.data.model;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;
import ru.hutoroff.jagpb.data.mongo.dao.PollDocument;

import java.util.Date;

@Embedded
public class Voter implements PollDocument {
    @Property(F_OPTION_VOTER_DATE)
    private Date date;

    @Property(F_OPTION_VOTER_USERNAME)
    private String username;

    @Property(F_OPTION_VOTER_FIRSTNAME)
    private String firstName;

    @Property(F_OPTION_VOTER_SECONDNAME)
    private String secondName;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
