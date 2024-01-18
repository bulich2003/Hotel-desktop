package org.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class Employee extends Person {

    @Column(name = "post")
    private String post;

    @Column(name = "salary")
    private int salary;

    public Employee() {}

    public Employee(String name, String surname, String post, int salary) {
        this.name = name;
        this.surname = surname;
        this.post = post;
        this.salary = salary;
    }

    public String getPost() { return post; }
    public void setPost(String post) { this.post = post; }
    public int getSalary() { return salary; }
    public void setSalary(int salary) { this.salary = salary; }
}