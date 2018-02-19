package ru.hutoroff.jagpb.business;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hutoroff.jagpb.data.model.PollDO;
import ru.hutoroff.jagpb.data.model.PollOption;
import ru.hutoroff.jagpb.data.mongo.dao.PollDAO;

import java.util.List;

@Service
public class PollService {
    private static final Logger LOG = LoggerFactory.getLogger(PollService.class);

    private final PollDAO pollDAO;

    @Autowired
    public PollService(PollDAO pollDAO) {
        this.pollDAO = pollDAO;
    }

    public ObjectId createPoll(String title, List<PollOption> options, Integer authorId) {
        PollDO newPoll = new PollDO();

        newPoll.setAuthorId(authorId);
        newPoll.setTitle(title);
        newPoll.setOptions(options);

        Key<PollDO> key = pollDAO.save(newPoll);
        return (ObjectId) key.getId();
    }

    public PollDO createAndGetBackPoll(String title, List<PollOption> options, Integer authorId) {
        ObjectId id = this.createPoll(title, options, authorId);
        return this.getPoll(id);
    }

    public PollDO getPoll(ObjectId id) {
        return pollDAO.get(id);
    }
}
