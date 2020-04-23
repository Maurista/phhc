package com;

import com.mongo_java.pojo.Person;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author Administrator
 */
@SpringBootApplication
public class SpringbootApp implements CommandLineRunner {

    /*private final MongoTemplate mongoTemplate;

    private final MongoTemplate mongoTemplate1;

    @Autowired
    public SpringbootApp( @Qualifier("mongoTemplate") MongoTemplate mongoTemplate, @Qualifier("mongoTemplate1") MongoTemplate mongoTemplate1) {
        this.mongoTemplate = mongoTemplate;
        this.mongoTemplate1 = mongoTemplate1;
    }*/
    private final MongoTemplate mongoTemplate;

    @Autowired
    public SpringbootApp(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApp.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        /*mongoTemplate.insert(new Person());
        mongoTemplate1.insert(new Person());*/
        mongoTemplate.insert(new Person());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!" + mongoTemplate.getDb().getName());;
    }
}