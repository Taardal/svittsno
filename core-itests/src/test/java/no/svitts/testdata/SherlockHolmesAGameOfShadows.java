package no.svitts.testdata;

import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.person.Gender;
import no.svitts.core.person.Job;
import no.svitts.core.person.Person;
import no.svitts.core.util.Id;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SherlockHolmesAGameOfShadows extends Movie {

    public SherlockHolmesAGameOfShadows() {
        super(Id.get());
        setName("Sherlock Holmes");
        setImdbId("");
        setTagline("Nothing escapes him.");
        setOverview("Eccentric consulting detective Sherlock Holmes and Doctor John Watson battle to bring down a new nemesis and unravel a deadly plot that could destroy England.");
        setRuntime(128);
        setReleaseDate(Date.valueOf("2009-12-24"));
        setGenres(getSherlockHolmesGenres());
        setCast(getSherlockHolmesCast());
        setCrew(getSherlockHolmesCrew());
    }

    private List<Genre> getSherlockHolmesGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(Genre.ACTION);
        genres.add(Genre.ADVENTURE);
        genres.add(Genre.CRIME);
        return genres;
    }

    private List<Person> getSherlockHolmesCast() {
        List<Person> cast = new ArrayList<>();
        cast.add(new Person(Id.get(), "Robert Downey Jr.", Date.valueOf("1965-04-04"), Gender.MALE));
        cast.add(new Person(Id.get(), "Jude Law", Date.valueOf("1979-12-29"), Gender.MALE));
        cast.add(new Person(Id.get(), "Rachel McAdams", Date.valueOf("1978-11-17"), Gender.FEMALE));
        cast.add(new Person(Id.get(), "Mark Strong", Date.valueOf("1963-08-05"), Gender.MALE));
        return cast;
    }

    private Map<Job, Person> getSherlockHolmesCrew() {
        Map<Job, Person> crew = new HashMap<>();
        crew.put(Job.DIRECTOR, new Person(Id.get(), "Guy Ritchie", Date.valueOf("1968-09-10"), Gender.MALE));
        return crew;
    }
}
