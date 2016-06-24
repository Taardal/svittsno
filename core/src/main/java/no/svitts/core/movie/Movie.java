package no.svitts.core.movie;

import no.svitts.core.date.ReleaseDate;
import no.svitts.core.genre.Genre;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;
import java.util.List;

@Entity
@Table(name = "movie")
public class Movie {

    public static final int ID_MAX_LENGTH = 255;
    public static final int NAME_MAX_LENGTH = 255;
    public static final int IMDB_ID_MAX_LENGTH = 255;
    public static final int TAGLINE_MAX_LENGTH = 255;
    public static final int OVERVIEW_MAX_LENGTH = 510;
    public static final int VIDEO_FILE_PATH_MAX_LENGTH = 255;
    public static final int POSTER_IMAGE_FILE_PATH_MAX_LENGTH = 255;
    public static final int BACKDROP_IMAGE_FILE_PATH_MAX_LENGTH = 255;

    private String id;
    private String name;
    private String imdbId;
    private String tagline;
    private String overview;
    private int runtime;
    private ReleaseDate releaseDate;
    private List<Genre> genres;
    private File videoFile;
    private File posterImageFile;
    private File backdropImageFile;

    public Movie(String id, String name, String imdbId, String tagline, String overview, int runtime, ReleaseDate releaseDate, List<Genre> genres, File videoFile, File posterImageFile, File backdropImageFile) {
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
        if (object != null && getClass() == object.getClass()) {
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

    @Id
    @Column(name = "id", nullable = false, length = ID_MAX_LENGTH)
    public String getId() {
        return id;
    }

    @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "imdb_id", length = IMDB_ID_MAX_LENGTH)
    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    @Column(name = "tagline", length = TAGLINE_MAX_LENGTH)
    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    @Column(name = "overview", length = OVERVIEW_MAX_LENGTH)
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Column(name = "runtime")
    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public ReleaseDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(ReleaseDate releaseDate) {
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