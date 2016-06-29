package no.svitts.core.builder;

import no.svitts.core.date.ReleaseDate;
import no.svitts.core.genre.Genre;
import no.svitts.core.id.Id;
import no.svitts.core.movie.Movie;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class MovieBuilder implements Builder<Movie> {

    private String id;
    private String name;
    private String imdbId;
    private String tagline;
    private String overview;
    private int runtime;
    private ReleaseDate releaseDate;
    private Set<Genre> genres;
    private File videoFile;
    private File posterImageFile;
    private File backdropImageFile;

    public MovieBuilder() {
        id = Id.get();
        name = "name";
        imdbId = "imdbId";
        tagline = "tagline";
        overview = "overview";
        runtime = 0;
        releaseDate = new ReleaseDate(2016, 1, 1);
        genres = getDefaultGenres();
        videoFile = new File("videoFilePath");
        posterImageFile = new File("posterImageFilePath");
        backdropImageFile = new File("backdropImageFilePath");
    }

    @Override
    public Movie build() {
        return new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres, videoFile, posterImageFile, backdropImageFile);
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

    public MovieBuilder releaseDate(ReleaseDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public MovieBuilder genres(Set<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public MovieBuilder videoFile(File videoFile) {
        this.videoFile = videoFile;
        return this;
    }

    public MovieBuilder videoFile(String path) {
        this.videoFile = new File(path);
        return this;
    }

    public MovieBuilder posterImageFile(File posterImageFile) {
        this.posterImageFile = posterImageFile;
        return this;
    }

    public MovieBuilder posterImageFile(String path) {
        this.posterImageFile = new File(path);
        return this;
    }

    public MovieBuilder backdropImageFile(File backdropImageFile) {
        this.backdropImageFile = backdropImageFile;
        return this;
    }

    public MovieBuilder backdropImageFile(String path) {
        this.backdropImageFile = new File(path);
        return this;
    }

    private Set<Genre> getDefaultGenres() {
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.FILM_NOIR);
        genres.add(Genre.MUSICAL);
        return genres;
    }

}
