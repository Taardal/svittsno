package no.svitts.core.movie;

import no.svitts.core.id.Id;

import java.util.ArrayList;
import java.util.List;

public enum Genre {

    ACTION(Id.get(), "Action"),
    ADVENTURE(Id.get(), "Adventure"),
    ANIMATION(Id.get(), "Animation"),
    BIOGRAPHY(Id.get(), "Biography"),
    COMEDY(Id.get(), "Comedy"),
    CRIME(Id.get(), "Crime"),
    DOCUMENTARY(Id.get(), "Documentary"),
    DRAMA(Id.get(), "Drama"),
    FAMILY(Id.get(), "Family"),
    FANTASY(Id.get(), "Fantasy"),
    FILM_NOIR(Id.get(), "Film-Noir"),
    HISTORY(Id.get(), "History"),
    HORROR(Id.get(), "Horror"),
    MUSIC(Id.get(), "Music"),
    MUSICAL(Id.get(), "Musical"),
    MYSTERY(Id.get(), "Mystery"),
    ROMANCE(Id.get(), "Romance"),
    SCI_FI(Id.get(), "Sci-Fi"),
    SPORT(Id.get(), "Sport"),
    THRILLER(Id.get(), "Thriller"),
    WAR(Id.get(), "War"),
    WESTERN(Id.get(), "Western");

    private String id;
    private String value;

    Genre(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public static String toString(List<Genre> genres) {
        String genresString = "";
        for (int i = 0; i < genres.size(); i++) {
            if (i > 0) {
                genresString += ",";
            }
            genresString += genres.get(i);
        }
        return genresString;
    }

    public static List<Genre> fromString(String genresString) {
        String[] genreStrings = genresString.split(",");
        List<Genre> genres = new ArrayList<>();
        for (String genreString : genreStrings) {
            genres.add(Genre.valueOf(genreString));
        }
        return genres;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

}
