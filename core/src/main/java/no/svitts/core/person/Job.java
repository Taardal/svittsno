package no.svitts.core.person;

public enum Job {

    UNKNOWN("Unknown"),
    ACTOR("Actor"),
    DIRECTOR("Director"),
    PRODUCER("Producer"),
    EXECUTIVE_PRODUCER("Executive Producer"),
    WRITER("Writer"),
    COMPOSER("Composer");

    private String value;

    Job(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
