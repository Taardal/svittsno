package no.svitts.core.person;

import no.svitts.core.date.KeyDate;

public class UnknownPerson extends Person {

    public static final String ID = "unknown-person-id";

    public UnknownPerson() {
        super(ID);
        setFirstName("Jane");
        setLastName("Doe");
        setDateOfBirth(new KeyDate(1975, 1, 1));
        setGender(Gender.FEMALE);
    }

}
