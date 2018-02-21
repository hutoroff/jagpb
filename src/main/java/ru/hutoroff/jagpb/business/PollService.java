package ru.hutoroff.jagpb.business;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.User;
import ru.hutoroff.jagpb.data.model.PollDO;
import ru.hutoroff.jagpb.data.model.PollOption;
import ru.hutoroff.jagpb.data.model.Voter;
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

    public PollDO createAndGetBackPoll(String title, List<PollOption> options, Integer authorId) {
        ObjectId id = this.createPoll(title, options, authorId);
        return this.getPoll(id);
    }

    public PollDO getPoll(ObjectId id) {
        return pollDAO.get(id);
    }

    public boolean vote(final ObjectId pollId, final String optionId, final User user) {
        PollDO poll = getPoll(pollId);
        if (poll == null) {
            throw new IllegalStateException("Poll with _id " + pollId.toString() + " does not exist");
        }

        final Voter voter = prepareVoter(user);
        boolean wasVoted = false;
        for (PollOption pollOption : poll.getOptions()) {
            if (pollOption.getId().equals(optionId) && !pollOption.getVoters().contains(voter)) {
                pollOption.getVoters().add(voter);
                wasVoted = true;
            } else {
                pollOption.getVoters().removeIf(el -> el.getId().equals(voter.getId()));
            }
        }

        if (!wasVoted) {
            LOG.warn("Option (id: {}) to vote for was not found in poll with _id: {}", optionId, pollId.toString());
            return false;
        }

        pollDAO.save(poll);
        return true;
    }

    private ObjectId createPoll(String title, List<PollOption> options, Integer authorId) {
        PollDO newPoll = new PollDO();

        newPoll.setAuthorId(authorId);
        newPoll.setTitle(title);
        newPoll.setOptions(options);

        Key<PollDO> key = pollDAO.save(newPoll);
        return (ObjectId) key.getId();
    }

    private Voter prepareVoter(User user) {
        Voter result = new Voter();
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setUsername(user.getUserName());
        result.setId(user.getId());

        return result;
    }
}
