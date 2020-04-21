package com;

import com.mongo_java.pojo.Person;
import com.mongodb.client.MongoClient;
import com.service.impl.InsertImpl;
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

    private final InsertImpl insertImpl;

    @Autowired
    public SpringbootApp(InsertImpl insertImpl) {
        this.insertImpl = insertImpl;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApp.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        insertImpl.insertInto(new Person("li", null ,null));
    }
}