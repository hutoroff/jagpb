package ru.hutoroff.jagpb.data.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hutoroff.jagpb.data.model.PollDO;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class MongoContextConfiguration {
    private static final String BASE = "admin";

    private final MongoConfiguration mongoConfiguration;

    @Autowired
    public MongoContextConfiguration(MongoConfiguration mongoConfiguration) {
        this.mongoConfiguration = mongoConfiguration;
    }

    @PreDestroy
    public void onDestroy() {
        mongoClient().close();
    }

    @Bean
    public MongoClient mongoClient() {
        MongoClientOptions.Builder options = MongoClientOptions.builder()   //TODO: make connection options configurable
                .connectionsPerHost(4)
                .maxConnectionIdleTime(60000)
                .maxConnectionLifeTime(120000);

        ServerAddress serverAddress = new ServerAddress(mongoConfiguration.getHost(), mongoConfiguration.getPort());

        MongoCredential credential = MongoCredential.createCredential(mongoConfiguration.getUser(), BASE, mongoConfiguration.getPassword().toCharArray());

        return new MongoClient(serverAddress, Collections.singletonList(credential), options.build());
    }

    @Bean
    public Morphia morphia() {
        Set<Class> classesToMap = new HashSet<Class>() {
            {
                add(PollDO.class);
            }
        };
        return new Morphia(classesToMap);
    }

    @Bean
    public Datastore datastore() {
        return morphia().createDatastore(mongoClient(), mongoConfiguration.getDb());
    }

}
