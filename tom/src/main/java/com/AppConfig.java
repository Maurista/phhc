package com;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class AppConfig {

    @Bean
    public MongoClient mongoClient(){
        return MongoClients.create();
    }

    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoClient(),"dbaaa");
    }

    @Bean
    MongoTransactionManager mongoTransactionManager() {
        return new MongoTransactionManager(mongoTemplate().getMongoDbFactory());
    }

}
