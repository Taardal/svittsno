package no.svitts.core.builder;

import no.svitts.core.date.KeyDate;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.util.Id;

import java.util.ArrayList;
import java.util.List;

public class MovieBuilder implements Builder<Movie> {

    private String id;
    private String name;
    private String imdbId;
    private String tagline;
    private String overview;
    private int runtime;
    private KeyDate releaseDate;
    private List<Genre> genres;

    public MovieBuilder() {
        id = Id.get();
        name = "name";
        imdbId = "imdbId";
        tagline = "tagline";
        overview = "overview";
        runtime = 120;
        releaseDate = new KeyDate();
        genres = new ArrayList<>();
    }

    @Override
    public Movie build() {
        Movie movie = new Movie(id, name);
        movie.setImdbId(imdbId);
        movie.setTagline(tagline);
        movie.setOverview(overview);
        movie.setRuntime(runtime);
        movie.setReleaseDate(releaseDate);
        movie.setGenres(genres);
        return movie;
    }

    public MovieBuilder id(String id) {
        this.id = id;
        return this;
    }

    public MovieBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MovieBuilder imdbId(String imdbId) {
        this.imdbId = imdbId;
        return this;
    }

    public MovieBuilder tagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public MovieBuilder overview(String overview) {
        this.overview = overview;
        return this;
    }

    public MovieBuilder runtime(int runtime) {
        this.runtime = runtime;
        return this;
    }

    public MovieBuilder releaseDate(KeyDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public MovieBuilder genres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public MovieBuilder sherlockHolmes() {
        id = Id.get();
        name = "Sherlock Holmes";
        imdbId = "tt0988045";
        tagline = "Nothing escapes him.";
        overview = "Eccentric consulting detective Sherlock Holmes and Doctor John Watson battle to bring down a new nemesis and unravel a deadly plot that could destroy England.";
        runtime = 128;
        releaseDate = new KeyDate(2009, 12, 24);
        genres = getSherlockHolmesGenres();
        return this;
    }

    public MovieBuilder sherlockHolmesAGameOfShadows() {
        id = Id.get();
        name = "Sherlock Holmes: A Game of Shadows";
        imdbId = "tt1515091";
        tagline = "The Game is Afoot.";
        overview = "There is a new criminal mastermind at large--Professor Moriarty--and not only is he Holmes’ intellectual equal, but his capacity for evil and lack of conscience may give him an advantage over the detective.";
        runtime = 129;
        releaseDate = new KeyDate(2011, 12, 16);
        genres = getSherlockHolmesAGameOfShadowsGenres();
        return this;
    }

    private List<Genre> getSherlockHolmesGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(Genre.ACTION);
        genres.add(Genre.ADVENTURE);
        genres.add(Genre.CRIME);
        genres.add(Genre.MYSTERY);
        genres.add(Genre.THRILLER);
        return genres;
    }

    private List<Genre> getSherlockHolmesAGameOfShadowsGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(Genre.ACTION);
        genres.add(Genre.ADVENTURE);
        genres.add(Genre.CRIME);
        genres.add(Genre.MYSTERY);
        return genres;
    }

    //    private Map<Job, List<Person>> getMoviePersons() {
//        Map<Job, List<Person>> persons = new HashMap<>();
//        persons.put(Job.ACTOR, getActors());
//        persons.put(Job.DIRECTOR, getDirectors());
//        persons.put(Job.WRITER, getWriters());
//        return persons;
//    }
//
//    private List<Person> getActors() {
//        List<Person> cast = new ArrayList<>();
//        cast.add(new Person(Id.get(), "Robert", "Downey Jr.", new KeyDate(1965, 4, 4), Gender.MALE));
//        cast.add(new Person(Id.get(), "Jude", "Law", new KeyDate(1979, 12, 29), Gender.MALE));
//        cast.add(new Person(Id.get(), "Rachel", "McAdams", new KeyDate(1978, 11, 17), Gender.FEMALE));
//        cast.add(new Person(Id.get(), "Mark", "Strong", new KeyDate(1963, 8, 5), Gender.MALE));
//        return cast;
//    }
//
//    private List<Person> getDirectors() {
//        List<Person> persons = new ArrayList<>();
//        persons.add(new Person(Id.get(), "Guy", "Ritchie", new KeyDate(1968, 9, 10), Gender.MALE));
//        return persons;
//    }
//
//    private List<Person> getWriters() {
//        List<Person> persons = new ArrayList<>();
//        persons.add(new Person(Id.get(), "Arthur", "Conan Doyle", new KeyDate(1859, 5, 22), Gender.MALE));
//        return persons;
//    }

}
