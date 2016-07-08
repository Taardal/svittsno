package no.svitts.core.movie;

import no.svitts.core.constraint.Length;
import no.svitts.core.constraint.NonNegative;
import no.svitts.core.constraint.NotNullOrEmpty;
import no.svitts.core.constraint.ValidCharacters;
import no.svitts.core.date.ReleaseDate;
import no.svitts.core.file.MediaFile;
import no.svitts.core.genre.Genre;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Set;

@Entity
@Table(name = "movie")
public class Movie {

    public static final int ID_MAX_LENGTH = 255;
    public static final int NAME_MAX_LENGTH = 255;
    public static final int IMDB_ID_MAX_LENGTH = 255;
    public static final int TAGLINE_MAX_LENGTH = 255;
    public static final int OVERVIEW_MAX_LENGTH = 510;

    private String id;
    private String name;
    private String imdbId;
    private String tagline;
    private String overview;
    private int runtime;
    private ReleaseDate releaseDate;
    private Set<Genre> genres;
    private MediaFile videoFile;
    private MediaFile posterImageFile;
    private MediaFile backdropImageFile;

    private Movie() {
    }

    public Movie(String id, String name, String imdbId, String tagline, String overview, int runtime, ReleaseDate releaseDate, Set<Genre> genres, MediaFile videoFile, MediaFile posterImageFile, MediaFile backdropImageFile) {
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
            return (id == null && movie.getId() == null) || id.equals(movie.getId())
                    && (name == null && movie.getName() == null) || name.equals(movie.getName())
                    && (imdbId == null && movie.getImdbId() == null) || imdbId.equals(movie.getImdbId())
                    && (tagline == null && movie.getTagline() == null) || tagline.equals(movie.getTagline())
                    && (overview == null && movie.getOverview() == null) || overview.equals(movie.getOverview())
                    && runtime == movie.getRuntime()
                    && (releaseDate == null && movie.getReleaseDate() == null) || releaseDate.equals(movie.getReleaseDate())
                    && (genres == null && movie.getGenres() == null) || genres.equals(movie.genres)
                    && (videoFile == null && movie.getVideoFile() == null) || videoFile.equals(movie.getVideoFile())
                    && (posterImageFile == null && movie.getPosterImageFile() == null) || posterImageFile.equals(movie.getPosterImageFile())
                    && (backdropImageFile == null && movie.getBackdropImageFile() == null) || backdropImageFile.equals(movie.getBackdropImageFile());
        } else {
            return false;
        }
    }

    @NotNullOrEmpty
    @ValidCharacters
    @Length(length = ID_MAX_LENGTH)
    @Id
    @Column(name = "id", nullable = false, length = ID_MAX_LENGTH)
    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    @NotNullOrEmpty
    @ValidCharacters
    @Length(length = NAME_MAX_LENGTH)
    @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public MediaFile getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(MediaFile videoFile) {
        this.videoFile = videoFile;
    }

    @Valid
    @Embedded
    @Column(name = "poster_image_file")
    @AttributeOverrides({
            @AttributeOverride(name = "path", column = @Column(name = "poster_image_file_path"))
    })
    public MediaFile getPosterImageFile() {
        return posterImageFile;
    }

    public void setPosterImageFile(MediaFile posterImageFile) {
        this.posterImageFile = posterImageFile;
    }

    @Valid
    @Embedded
    @Column(name = "backdrop_image_file")
    @AttributeOverrides({
            @AttributeOverride(name = "path", column = @Column(name = "backdrop_image_file_path"))
    })
    public MediaFile getBackdropImageFile() {
        return backdropImageFile;
    }

    public void setBackdropImageFile(MediaFile backdropImageFile) {
        this.backdropImageFile = backdropImageFile;
    }
}