package ru.hutoroff.jagpb.data.model;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;
import ru.hutoroff.jagpb.data.mongo.dao.PollDocument;

import java.util.List;

@Embedded
public class PollOption implements PollDocument {
    @Property(F_OPTION_TITLE)
    private String title;

    @Embedded
    private List<Voter> voters;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Voter> getVoters() {
        return voters;
    }

    public void setVoters(List<Voter> voters) {
        this.voters = voters;
    }
}
