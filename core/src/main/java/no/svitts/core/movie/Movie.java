package no.svitts.core.movie;

import java.sql.Date;

public class Movie {

    private final int id;
    private int runtime;
    private String imdbId;
    private String name;
    private String tagline;
    private String overview;
    private Date releaseDate;
    private Date added;
    private Date lastPlayed;
//    private File movieFile;
//    private File backdropFile;

    public Movie(int id) {
        this.id = id;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", runtime=" + runtime +
                ", imdbId='" + imdbId + '\'' +
                ", name='" + name + '\'' +
                ", tagline='" + tagline + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate=" + releaseDate +
                ", added=" + added +
                ", lastPlayed=" + lastPlayed +
                '}';
    }
}
