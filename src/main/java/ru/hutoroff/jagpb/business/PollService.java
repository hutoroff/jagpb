package ru.hutoroff.jagpb.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hutoroff.jagpb.data.model.PollDO;
import ru.hutoroff.jagpb.data.mongo.dao.PollDAO;

@Service
public class PollService {
    private static final Logger LOG = LoggerFactory.getLogger(PollService.class);

    private final PollDAO pollDAO;

    @Autowired
    public PollService(PollDAO pollDAO) {
        this.pollDAO = pollDAO;
    }

    public void createPoll(String title, Integer authorId) {
        PollDO newPoll = new PollDO();
        newPoll.setAuthorId(authorId);
        newPoll.setTitle(title);

        pollDAO.save(newPoll);
    }
}
