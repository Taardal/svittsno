package no.svitts.core.genre;

import java.util.ArrayList;
import java.util.List;

public enum Genre {

    ACTION(1, "Action"),
    ADVENTURE(2, "Adventure"),
    ANIMATION(3, "Animation"),
    BIOGRAPHY(4, "Biography"),
    COMEDY(5, "Comedy"),
    CRIME(6, "Crime"),
    DOCUMENTARY(7, "Documentary"),
    DRAMA(8, "Drama"),
    FAMILY(9, "Family"),
    FANTASY(10, "Fantasy"),
    FILM_NOIR(11, "Film-Noir"),
    HISTORY(12, "History"),
    HORROR(13, "Horror"),
    MUSIC(14, "Music"),
    MUSICAL(15, "Musical"),
    MYSTERY(16, "Mystery"),
    ROMANCE(17, "Romance"),
    SCI_FI(18, "Sci-Fi"),
    SPORT(19, "Sport"),
    THRILLER(20, "Thriller"),
    WAR(21, "War"),
    WESTERN(22, "Western");

    public static final int GENRE_MAX_LENGTH = 255;

    private int id;
    private String value;

    Genre(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
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
        if (genresString != null && !genresString.isEmpty()) {
            return getGenres(genresString);
        } else {
            return new ArrayList<>();
        }
    }

    private static List<Genre> getGenres(String genresString) {
        String[] genreStrings = genresString.split(",");
        List<Genre> genres = new ArrayList<>();
        for (String genreString : genreStrings) {
            genres.add(Genre.valueOf(genreString));
        }
        return genres;
    }
}
