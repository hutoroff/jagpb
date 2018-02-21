package ru.hutoroff.jagpb.data.model;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;
import ru.hutoroff.jagpb.data.mongo.dao.PollDocument;

import java.util.Date;
import java.util.Objects;

@Embedded
public class Voter implements PollDocument {
    @Property(F_OPTION_VOTER_ID)
    private Integer id;

    @Property(F_OPTION_VOTER_DATE)
    private Date date;

    @Property(F_OPTION_VOTER_USERNAME)
    private String username;

    @Property(F_OPTION_VOTER_FIRSTNAME)
    private String firstName;

    @Property(F_OPTION_VOTER_LASTNAME)
    private String lastName;

    public Voter() {
        this.date = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Voter)) return false;
        Voter voter = (Voter) o;
        return Objects.equals(getId(), voter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
