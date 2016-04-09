package no.svitts.core.person;

import no.svitts.core.date.KeyDate;

public class Person {

    private final String id;
    private String firstName;
    private String lastName;
    private KeyDate dateOfBirth;
    private Gender gender;

    public Person(String id) {
        this.id = id;
    }

    public Person(String id, String firstName, String lastName, KeyDate dateOfBirth, Gender gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public KeyDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(KeyDate dateOfBirth) {
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
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                '}';
    }
}
