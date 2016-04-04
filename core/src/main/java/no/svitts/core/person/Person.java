package no.svitts.core.person;

import java.sql.Date;

public class Person {

    private final String id;
    private String name;
    private Date dateOfBirth;
    private Gender gender;

    public Person(String id) {
        this.id = id;
    }

    public Person(String id, String name, Date dateOfBirth, Gender gender) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                '}';
    }
}
