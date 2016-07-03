package no.svitts.core.movie;

import no.svitts.core.date.ReleaseDate;
import no.svitts.core.genre.Genre;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.File;
import java.util.Set;

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
    private Set<Genre> genres;
    private File videoFile;
    private File posterImageFile;
    private File backdropImageFile;

    private Movie() {
    }

    public Movie(String id, String name, String imdbId, String tagline, String overview, int runtime, ReleaseDate releaseDate, Set<Genre> genres, File videoFile, File posterImageFile, File backdropImageFile) {
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

    private void setId(String id) {
        this.id = id;
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

    @Column(name = "release_date")
    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    public ReleaseDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(ReleaseDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    @Column(name = "video_file")
    public File getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(File videoFile) {
        this.videoFile = videoFile;
    }

    @Column(name = "poster_image_file")
    public File getPosterImageFile() {
        return posterImageFile;
    }

    public void setPosterImageFile(File posterImageFile) {
        this.posterImageFile = posterImageFile;
    }

    @Column(name = "backdrop_image_file")
    public File getBackdropImageFile() {
        return backdropImageFile;
    }

    public void setBackdropImageFile(File backdropImageFile) {
        this.backdropImageFile = backdropImageFile;
    }
}