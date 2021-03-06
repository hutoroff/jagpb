package ru.hutoroff.jagpb.data.model;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;
import ru.hutoroff.jagpb.data.mongo.dao.PollDocument;

import java.util.Date;
import java.util.List;

@Entity("polls")
public class PollDO extends BaseMongoDO implements PollDocument {
    @Property(F_TITLE)
    private String title;

    @Property(F_AUTHOR)
    private Integer authorId;

    @Property(F_CHAT)
    private Long chatId;

    @Property(F_CREATED)
    private Date created;

    @Embedded
    private List<PollOption> options;

    public PollDO() {
        this.created = new Date();
    }

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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<PollOption> getOptions() {
        return options;
    }

    public void setOptions(List<PollOption> options) {
        this.options = options;
    }
}
