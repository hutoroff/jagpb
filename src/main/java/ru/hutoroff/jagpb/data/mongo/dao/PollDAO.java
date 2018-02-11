package ru.hutoroff.jagpb.data.mongo.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hutoroff.jagpb.data.model.PollDO;
import ru.hutoroff.jagpb.data.model.PollOption;
import ru.hutoroff.jagpb.data.mongo.exceptions.DaoException;

import java.util.List;

@Service
public class PollDAO extends BasicDAO<PollDO, ObjectId> implements PollDocument {
    private final Datastore datastore;

    @Autowired
    public PollDAO(Datastore ds, Datastore datastore) {
        super(ds);
        this.datastore = datastore;
    }

    public List<PollDO> getPollsCreatedBy(String authorId) {
        Query<PollDO> authorQuery = datastore.createQuery(PollDO.class)
                .field(F_AUTHOR).equal(authorId);
        return authorQuery.asList();
    }

    public boolean addOptionToPoll(ObjectId pollId, PollOption option) throws DaoException {
        Query<PollDO> query = datastore.createQuery(PollDO.class)
                .field(F_ID).equal(pollId);
        UpdateOperations<PollDO> ops = datastore.createUpdateOperations(PollDO.class)
                .push(F_OPTIONS, option);
        UpdateResults update = datastore.update(query, ops);

        if (update.getUpdatedCount() != 1) {
            throw new DaoException("No documents was updated");
        }
        return true;
    }
}
