package ru.hutoroff.jagpb.data.model;

import org.mongodb.morphia.annotations.Embedded;

import java.util.List;

public class PollOption {
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
