package no.svitts.core.movie;

import no.svitts.core.constraint.Length;
import no.svitts.core.constraint.NonNegative;
import no.svitts.core.constraint.NotNullOrEmpty;
import no.svitts.core.constraint.ValidCharacters;
import no.svitts.core.date.ReleaseDate;
import no.svitts.core.file.VideoFile;
import no.svitts.core.genre.Genre;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Set;

@Entity
@Table(name = "movie")
public class Movie {

    public static final int ID_MAX_LENGTH = 255;
    public static final int TITLE_MAX_LENGTH = 255;
    public static final int IMDB_ID_MAX_LENGTH = 255;
    public static final int TAGLINE_MAX_LENGTH = 255;
    public static final int OVERVIEW_MAX_LENGTH = 510;
    public static final int POSTER_PATH_MAX_LENGTH = 255;
    public static final int BACKDROP_PATH_MAX_LENGTH = 255;

    private String id;
    private String title;
    private String imdbId;
    private String tagline;
    private String overview;
    private int runtime;
    private ReleaseDate releaseDate;
    private Set<Genre> genres;
    private VideoFile videoFile;
    private String posterPath;
    private String backdropPath;

    private Movie() {
    }

    public Movie(String id, String title, String imdbId, String tagline, String overview, int runtime, ReleaseDate releaseDate, Set<Genre> genres, VideoFile videoFile, String posterPath, String backdropPath) {
        this.id = id;
        this.title = title;
        this.imdbId = imdbId;
        this.tagline = tagline;
        this.overview = overview;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.videoFile = videoFile;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", tagline='" + tagline + '\'' +
                ", overview='" + overview + '\'' +
                ", runtime=" + runtime +
                ", releaseDate=" + releaseDate +
                ", genres=" + genres +
                ", videoFile=" + videoFile +
                ", posterPath=" + posterPath +
                ", backdropPath=" + backdropPath +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && getClass() == object.getClass()) {
            Movie movie = (Movie) object;
            return (id == null && movie.getId() == null) || id.equals(movie.getId())
                    && (title == null && movie.getTitle() == null) || title.equals(movie.getTitle())
                    && (imdbId == null && movie.getImdbId() == null) || imdbId.equals(movie.getImdbId())
                    && (tagline == null && movie.getTagline() == null) || tagline.equals(movie.getTagline())
                    && (overview == null && movie.getOverview() == null) || overview.equals(movie.getOverview())
                    && runtime == movie.getRuntime()
                    && (releaseDate == null && movie.getReleaseDate() == null) || releaseDate.equals(movie.getReleaseDate())
                    && (genres == null && movie.getGenres() == null) || genres.equals(movie.genres)
                    && (videoFile == null && movie.getVideoFile() == null) || videoFile.equals(movie.getVideoFile())
                    && (posterPath == null && movie.getPosterPath() == null) || posterPath.equals(movie.getPosterPath())
                    && (backdropPath == null && movie.getBackdropPath() == null) || backdropPath.equals(movie.getBackdropPath());
        } else {
            return false;
        }
    }

    @ValidCharacters
    @Length(length = ID_MAX_LENGTH)
    @Id
    @Column(name = "id", nullable = false, length = ID_MAX_LENGTH)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNullOrEmpty
    @ValidCharacters
    @Length(length = TITLE_MAX_LENGTH)
    @Column(name = "title", nullable = false, length = TITLE_MAX_LENGTH)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ValidCharacters
    @Length(length = IMDB_ID_MAX_LENGTH)
    @Column(name = "imdb_id", length = IMDB_ID_MAX_LENGTH)
    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    @ValidCharacters
    @Length(length = TAGLINE_MAX_LENGTH)
    @Column(name = "tagline", length = TAGLINE_MAX_LENGTH)
    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    @ValidCharacters
    @Length(length = OVERVIEW_MAX_LENGTH)
    @Column(name = "overview", length = OVERVIEW_MAX_LENGTH)
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @NonNegative
    @Column(name = "runtime")
    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    @Embedded
    @Column(name = "release_date")
    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    public ReleaseDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(ReleaseDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Valid
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    @Valid
    @Embedded
    @Column(name = "video_file")
    @AttributeOverrides({
            @AttributeOverride(name = "path", column = @Column(name = "video_file_path"))
    })
    public VideoFile getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(VideoFile videoFile) {
        this.videoFile = videoFile;
    }

    @ValidCharacters
    @Length(length = POSTER_PATH_MAX_LENGTH)
    @Column(name = "poster_image_file")
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @ValidCharacters
    @Length(length = BACKDROP_PATH_MAX_LENGTH)
    @Column(name = "backdrop_path")
    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
}