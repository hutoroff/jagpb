package ru.hutoroff.jagpb.configuration;

import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import ru.hutoroff.jagpb.data.mongo.MongoConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class ApplicationConfigurationProvider {

    private static final String CONFIGURATION_FILE_NAME = "config.yml";
    private final ApplicationConfiguration configuration;

    public ApplicationConfigurationProvider() throws Exception {
        this.configuration = loadConfig();
    }

    public MongoConfiguration getMongoConfiguration() {
        return configuration.getMongo();
    }

    private ApplicationConfiguration loadConfig() throws IOException, URISyntaxException {
        Yaml yaml = new Yaml();
        URI jarUri = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
        File jar = new File(jarUri);
        final String fileToLoad = jar.getParentFile().toString() + File.separator + CONFIGURATION_FILE_NAME;
        try (InputStream is = new FileInputStream(fileToLoad)) {
            return yaml.loadAs(is, ApplicationConfiguration.class);
        }
    }
}
