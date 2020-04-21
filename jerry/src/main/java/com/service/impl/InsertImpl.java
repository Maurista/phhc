package com.service.impl;

import com.mongo_java.pojo.Person;
import com.service.Insert;
import com.service.InsertPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class InsertImpl implements Insert {

    private final
    InsertPerson insertPersonImpl;

    @Autowired
    public InsertImpl(InsertPerson insertPersonImpl) {
        this.insertPersonImpl = insertPersonImpl;
    }

    @Override
    public void insertInto(Person person) {
        insertPersonImpl.insert(person);
    }
}
