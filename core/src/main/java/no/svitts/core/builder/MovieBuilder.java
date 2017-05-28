package no.svitts.core.builder;

import no.svitts.core.date.ReleaseDate;
import no.svitts.core.file.SubtitleFile;
import no.svitts.core.file.VideoFile;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import no.svitts.core.util.Id;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MovieBuilder implements Builder<Movie> {

    private String id;
    private String title;
    private String imdbId;
    private String tagline;
    private String overview;
    private String language;
    private String edition;
    private int runtime;
    private ReleaseDate releaseDate;
    private Set<Genre> genres;
    private VideoFile videoFile;
    private Set<SubtitleFile> subtitleFiles;
    private String posterPath;
    private String backdropPath;

    public MovieBuilder() {
        id = Id.get();
        title = "";
        imdbId = "";
        tagline = "";
        overview = "";
        runtime = 0;
        releaseDate = null;
        genres = new HashSet<>();
        videoFile = new VideoFile("", "", "");
        subtitleFiles = new HashSet<>();
        posterPath = "";
        backdropPath = "";
    }

    @Override
    public Movie build() {
        Movie movie = new Movie(id);
        movie.setTitle(title);
        movie.setImdbId(imdbId);
        movie.setTagline(tagline);
        movie.setOverview(overview);
        movie.setLanguage(language);
        movie.setEdition(edition);
        movie.setRuntime(runtime);
        movie.setReleaseDate(releaseDate);
        movie.setGenres(genres);
        movie.setVideoFile(videoFile);
        movie.setSubtitleFiles(subtitleFiles);
        movie.setPosterPath(posterPath);
        movie.setBackdropPath(backdropPath);
        return movie;
    }

    public MovieBuilder id(String id) {
        this.id = id;
        return this;
    }

    public MovieBuilder title(String name) {
        this.title = name;
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

    public MovieBuilder language(String language) {
        this.language = language;
        return this;
    }

    public MovieBuilder edition(String edition) {
        this.edition = edition;
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

    public MovieBuilder genres(Genre... genres) {
        this.genres = Arrays.stream(genres).collect(Collectors.toSet());
        return this;
    }

    public MovieBuilder videoFile(VideoFile videoFile) {
        this.videoFile = videoFile;
        return this;
    }

    public MovieBuilder subtitleFiles(Set<SubtitleFile> subtitleFiles) {
        this.subtitleFiles = subtitleFiles;
        return this;
    }

    public MovieBuilder posterPath(String posterImageFile) {
        this.posterPath = posterImageFile;
        return this;
    }

    public MovieBuilder backdropPath(String backdropImageFile) {
        this.backdropPath = backdropImageFile;
        return this;
    }

    public MovieBuilder from(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.imdbId = movie.getImdbId();
        this.tagline = movie.getTagline();
        this.overview = movie.getOverview();
        this.language = movie.getLanguage();
        this.edition = movie.getEdition();
        this.runtime = movie.getRuntime();
        this.releaseDate = movie.getReleaseDate();
        this.genres = movie.getGenres();
        this.videoFile = movie.getVideoFile();
        this.subtitleFiles = movie.getSubtitleFiles();
        this.posterPath = movie.getPosterPath();
        this.backdropPath = movie.getBackdropPath();
        return this;
    }

    public MovieBuilder testMovie() {
        id = Id.get();
        title = "name";
        imdbId = "imdbId";
        tagline = "tagline";
        overview = "overview";
        runtime = 0;
        releaseDate = new ReleaseDate(2016, 1, 1);
        genres = getDefaultGenres();
        videoFile = new VideoFile("path/to/video/file", "1080p", "DTS-HD 5.1");
        subtitleFiles = getDefaultSubtitleFiles();
        posterPath = "path/to/poster/file";
        backdropPath = "path/to/backdrop/file";
        return this;
    }

    private Set<Genre> getDefaultGenres() {
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.FILM_NOIR);
        genres.add(Genre.MUSICAL);
        return genres;
    }

    private Set<SubtitleFile> getDefaultSubtitleFiles() {
        Set<SubtitleFile> subtitleFiles = new HashSet<>();
        subtitleFiles.add(new SubtitleFile("path/to/subtitle/file", "English"));
        subtitleFiles.add(new SubtitleFile("path/to/subtitle/file", "Norwegian"));
        return subtitleFiles;
    }

}
