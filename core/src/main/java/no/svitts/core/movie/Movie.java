package no.svitts.core.movie;

import java.io.File;
import java.util.List;

public class Movie {

    private int id;
    private String name;
    private String description;
    private int year;
    private int duration;
    private List<Genre> genres;
    private List<Person> persons;
    private List<MovieCollection> movieCollections;
    private File file;
}
