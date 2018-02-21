package ru.hutoroff.jagpb.data.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;
import ru.hutoroff.jagpb.data.mongo.dao.PollDocument;

import java.util.HashSet;
import java.util.Set;

@Embedded
public class PollOption implements PollDocument {

    @Property(F_OPTION_ID)
    private String id;

    @Property(F_OPTION_TITLE)
    private String title;

    @Embedded
    private Set<Voter> voters;

    public PollOption() {
        this.id = RandomStringUtils.random(8);
        this.voters = new HashSet<>();
    }

    public PollOption(String title) {
        this.id = RandomStringUtils.random(8);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Voter> getVoters() {
        return voters;
    }

    public void setVoters(Set<Voter> voters) {
        this.voters = voters;
    }

    public String getId() {
        return id;
    }
}
