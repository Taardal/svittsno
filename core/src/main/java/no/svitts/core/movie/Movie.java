package no.svitts.core.movie;

import no.svitts.core.date.KeyDate;
import no.svitts.core.file.ImageFile;
import no.svitts.core.file.VideoFile;
import no.svitts.core.person.Job;
import no.svitts.core.person.Person;

import java.util.List;
import java.util.Map;

public class Movie {

    private final String id;
    private String name;
    private String imdbId;
    private String tagline;
    private String overview;
    private int runtime;
    private KeyDate releaseDate;
    private List<Genre> genres;
    private Map<Job, Person> persons;
    private VideoFile videoFile;
    private ImageFile poster;
    private ImageFile backdrop;

    public Movie(String id) {
        this.id = id;
    }

    public Movie(String id, String name, String imdbId, String tagline, String overview, int runtime, KeyDate releaseDate, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.imdbId = imdbId;
        this.tagline = tagline;
        this.overview = overview;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public KeyDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(KeyDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Map<Job, Person> getPersons() {
        return persons;
    }

    public void setPersons(Map<Job, Person> persons) {
        this.persons = persons;
    }

    public VideoFile getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(VideoFile videoFile) {
        this.videoFile = videoFile;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", tagline='" + tagline + '\'' +
                ", overview='" + overview + '\'' +
                ", runtime=" + runtime +
                ", releaseDate=" + releaseDate +
                ", genres=" + genres +
                ", videoFile=" + videoFile +
                '}';
    }
}
