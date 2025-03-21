package com.Harum.Harum;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        String mongoUri = String.format("mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority&appName=JavaProjects",
                EnvConfig.get("MONGO_USER"),
                EnvConfig.get("MONGO_PASS"),
                EnvConfig.get("MONGO_HOST"),
                EnvConfig.get("MONGO_DB"));

        return new MongoTemplate(MongoClients.create(mongoUri), EnvConfig.get("MONGO_DB"));
    }
}
