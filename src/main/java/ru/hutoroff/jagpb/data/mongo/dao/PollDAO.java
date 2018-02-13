package ru.hutoroff.jagpb.data.mongo.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.hutoroff.jagpb.data.model.PollDO;
import ru.hutoroff.jagpb.data.model.PollOption;
import ru.hutoroff.jagpb.data.mongo.exceptions.DaoException;

import java.util.List;

@Repository
public class PollDAO extends BasicDAO<PollDO, ObjectId> implements PollDocument {

    @Autowired
    public PollDAO(Datastore ds) {
        super(ds);
    }

    public List<PollDO> getPollsCreatedBy(String authorId) {
        Query<PollDO> authorQuery = getDatastore().createQuery(PollDO.class)
                .field(F_AUTHOR).equal(authorId);
        return authorQuery.asList();
    }

    public boolean addOptionToPoll(ObjectId pollId, PollOption option) throws DaoException {
        Query<PollDO> query = getDatastore().createQuery(PollDO.class)
                .field(F_ID).equal(pollId);
        UpdateOperations<PollDO> ops = getDatastore().createUpdateOperations(PollDO.class)
                .push(F_OPTIONS, option);
        UpdateResults update = getDatastore().update(query, ops);

        if (update.getUpdatedCount() != 1) {
            throw new DaoException("No documents was updated");
        }
        return true;
    }
}
