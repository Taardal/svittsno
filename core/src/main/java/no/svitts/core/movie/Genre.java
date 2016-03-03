package no.svitts.core.movie;

public enum Genre {

    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation");

    private String value;

    Genre(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
