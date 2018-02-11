package ru.hutoroff.jagpb.data.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import ru.hutoroff.jagpb.data.mongo.dao.MongoDocument;

public class BaseMongoDO implements MongoDocument {
    @Id
    @Property(F_ID)
    private ObjectId id;

    @Property(F_VERSION)
    private Long version;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
