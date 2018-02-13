package ru.hutoroff.jagpb.data.model;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;
import ru.hutoroff.jagpb.data.mongo.dao.PollDocument;

import java.util.List;

@Entity("polls")
public class PollDO extends BaseMongoDO implements PollDocument {
    @Property(F_TITLE)
    private String title;

    @Property(F_AUTHOR)
    private Integer authorId;

    @Embedded
    private List<PollOption> options;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public List<PollOption> getOptions() {
        return options;
    }

    public void setOptions(List<PollOption> options) {
        this.options = options;
    }
}
