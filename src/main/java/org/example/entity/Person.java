package org.example.entity;

import javax.persistence.*;

@MappedSuperclass
public class Person {

    @Id
    @Column(name = "idPerson")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int idPerson;

    @Column(name = "name")
    protected String name;

    @Column(name = "surname")
    protected String surname;

    protected Person() {}

    public int getIdPerson() { return idPerson; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
}




