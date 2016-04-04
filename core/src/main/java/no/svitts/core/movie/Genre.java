package no.svitts.core.movie;

public enum Genre {

    ACTION("Action"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    BIOGRAPHY("Biography"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FAMILY("Family"),
    FANTASY("Fantasy"),
    FILM_NOIR("Film-Noir"),
    HISTORY("History"),
    HORROR("Horror"),
    MUSIC("Music"),
    MUSICAL("Musical"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    SCI_FI("Sci-Fi"),
    SPORT("Sport"),
    THRILLER("Thriller"),
    WAR("War"),
    WESTERN("Western");

    private String value;

    Genre(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "value='" + value + '\'' +
                '}';
    }
}
