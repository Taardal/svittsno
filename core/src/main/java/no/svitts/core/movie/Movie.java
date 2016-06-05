package no.svitts.core.movie;

import no.svitts.core.date.KeyDate;

import java.io.File;
import java.util.List;

public class Movie {

    public static final int NAME_MAX_LENGTH = 255;

    private final String id;
    private String name;
    private String imdbId;
    private String tagline;
    private String overview;
    private int runtime;
    private KeyDate releaseDate;
    private List<Genre> genres;
    private File videoFile;
    private File posterImageFile;
    private File backdropImageFile;

    public Movie(String id, String name, String imdbId, String tagline, String overview, int runtime, KeyDate releaseDate, List<Genre> genres, File videoFile, File posterImageFile, File backdropImageFile) {
        this.id = id;
        this.name = name;
        this.imdbId = imdbId;
        this.tagline = tagline;
        this.overview = overview;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.videoFile = videoFile;
        this.posterImageFile = posterImageFile;
        this.backdropImageFile = backdropImageFile;
    }

    public static boolean isIdValid(String id) {
        return id != null && !id.equals("") && id.length() <= NAME_MAX_LENGTH;
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
                ", posterImageFile=" + posterImageFile +
                ", backdropImageFile=" + backdropImageFile +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (getClass() == object.getClass()) {
            Movie movie = (Movie) object;
            return id.equals(movie.getId())
                    && name.equals(movie.getName())
                    && imdbId.equals(movie.getImdbId())
                    && tagline.equals(movie.getTagline())
                    && overview.equals(movie.getOverview())
                    && runtime == movie.getRuntime()
                    && releaseDate.equals(movie.getReleaseDate())
                    && genres.equals(movie.genres)
                    && videoFile.equals(movie.getVideoFile())
                    && posterImageFile.equals(movie.getPosterImageFile())
                    && backdropImageFile.equals(movie.getBackdropImageFile());
        } else {
            return false;
        }
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

    public File getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(File videoFile) {
        this.videoFile = videoFile;
    }

    public File getPosterImageFile() {
        return posterImageFile;
    }

    public void setPosterImageFile(File posterImageFile) {
        this.posterImageFile = posterImageFile;
    }

    public File getBackdropImageFile() {
        return backdropImageFile;
    }

    public void setBackdropImageFile(File backdropImageFile) {
        this.backdropImageFile = backdropImageFile;
    }
}