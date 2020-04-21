package com.service.impl;

import com.mongo_java.pojo.Person;
import com.service.InsertPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class InsertPersonImpl implements InsertPerson {

    final
    private MongoTemplate mongoTemplate;

    @Autowired
    public InsertPersonImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void insert(Person person) {
        mongoTemplate.insert(person);
    }
}
