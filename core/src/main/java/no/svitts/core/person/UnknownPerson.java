package no.svitts.core.person;

import java.sql.Date;

public class UnknownPerson extends Person {

    public UnknownPerson() {
        super("id-for-unknown-person");
        setName("Jane Doe");
        setDateOfBirth(Date.valueOf("1975-01-01"));
        setGender(Gender.FEMALE);
    }

}
