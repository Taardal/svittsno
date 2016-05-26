package no.svitts.core.testdatabuilder;

import no.svitts.core.date.KeyDate;
import no.svitts.core.file.ImageFile;
import no.svitts.core.file.ImageType;
import no.svitts.core.file.VideoFile;
import no.svitts.core.id.Id;
import no.svitts.core.movie.Genre;
import no.svitts.core.movie.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieTestDataBuilder implements TestDataBuilder<Movie> {

    private String id;
    private String name;
    private String imdbId;
    private String tagline;
    private String overview;
    private int runtime;
    private KeyDate releaseDate;
    private List<Genre> genres;
    private VideoFile videoFile;
    private Map<ImageType, ImageFile> imageFiles;

    public MovieTestDataBuilder() {
        id = Id.get();
        name = "name";
        imdbId = "imdbId";
        tagline = "tagline";
        overview = "overview";
        runtime = 0;
        releaseDate = new KeyDate();
        genres = getDefaultGenres();
        videoFile = new VideoFileTestDataBuilder().build();
        imageFiles = getDefaultImageFiles(new ImageFileTestDataBuilder());
    }

    @Override
    public Movie build() {
        return new Movie(id, name, imdbId, tagline, overview, runtime, releaseDate, genres, videoFile, imageFiles);
    }

    public MovieTestDataBuilder id(String id) {
        this.id = id;
        return this;
    }

    public MovieTestDataBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MovieTestDataBuilder imdbId(String imdbId) {
        this.imdbId = imdbId;
        return this;
    }

    public MovieTestDataBuilder tagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public MovieTestDataBuilder overview(String overview) {
        this.overview = overview;
        return this;
    }

    public MovieTestDataBuilder runtime(int runtime) {
        this.runtime = runtime;
        return this;
    }

    public MovieTestDataBuilder releaseDate(KeyDate releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public MovieTestDataBuilder genres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public MovieTestDataBuilder videoFile(VideoFile videoFile) {
        this.videoFile = videoFile;
        return this;
    }

    public MovieTestDataBuilder imageFiles(Map<ImageType, ImageFile> imageFiles) {
        this.imageFiles = imageFiles;
        return this;
    }

    public MovieTestDataBuilder sherlockHolmes() {
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

    public MovieTestDataBuilder sherlockHolmesAGameOfShadows() {
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

    private List<Genre> getDefaultGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(Genre.FILM_NOIR);
        genres.add(Genre.MUSICAL);
        return genres;
    }

    private Map<ImageType, ImageFile> getDefaultImageFiles(ImageFileTestDataBuilder imageFileTestDataBuilder) {
        Map<ImageType, ImageFile> imageFiles = new HashMap<>();
        imageFiles.put(ImageType.POSTER, imageFileTestDataBuilder.imageType(ImageType.POSTER).build());
        imageFiles.put(ImageType.BACKDROP, imageFileTestDataBuilder.imageType(ImageType.BACKDROP).build());
        return imageFiles;
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
}
