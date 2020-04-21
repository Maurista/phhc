package com.mongo_java.pojo;

import org.springframework.data.annotation.Id;

public class Employee {

    @Id
    private String id;

    private String employeeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Employee() {
    }

    public Employee(String employeeName) {
        this.employeeName = employeeName;
    }
}
