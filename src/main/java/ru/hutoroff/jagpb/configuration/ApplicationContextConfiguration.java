package ru.hutoroff.jagpb.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.hutoroff.jagpb.data.mongo.MongoConfiguration;
import ru.hutoroff.jagpb.data.mongo.MongoContextConfiguration;

@Configuration
@ComponentScan("ru.hutoroff.jagpb")
@Import(MongoContextConfiguration.class)
public class ApplicationContextConfiguration {

    private final ApplicationConfigurationProvider configurationProvider;

    @Autowired
    public ApplicationContextConfiguration(ApplicationConfigurationProvider configurationProvider) {
        this.configurationProvider = configurationProvider;
    }

    @Bean
    public MongoConfiguration mongoConfiguration() {
        return configurationProvider.getMongoConfiguration();
    }
}