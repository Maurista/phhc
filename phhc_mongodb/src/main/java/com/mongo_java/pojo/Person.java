package com.mongo_java.pojo;

import org.springframework.data.annotation.Id;

import java.util.List;

public final class Person {

    @Id
    private String personId;

    private String name;

    private Address addr;

    private List<Hobby> hobbyList;

    public Person(){
    }

    public Person(String name, Address addr, List<Hobby> hobbyList) {
        this.name = name;
        this.addr = addr;
        this.hobbyList = hobbyList;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId='" + personId + '\'' +
                ", name='" + name + '\'' +
                ", addr=" + addr +
                ", hobbyList=" + hobbyList +
                '}';
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddr() {
        return addr;
    }

    public void setAddr(Address addr) {
        this.addr = addr;
    }

    public List<Hobby> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<Hobby> hobbyList) {
        this.hobbyList = hobbyList;
    }
}