package no.svitts.core.file;

import java.io.File;

public class VideoFile extends File {

    private final String id;
    private String movieId;
    private String name;
    private String format;
    private String quality;
    private int size;

    public VideoFile(String pathname, String id, String movieId, String name, String format, String quality, int size) {
        super(pathname);
        this.id = id;
        this.movieId = movieId;
        this.name = name;
        this.format = format;
        this.quality = quality;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "VideoFile{" +
                "id='" + id + '\'' +
                ", movieId='" + movieId + '\'' +
                ", name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", quality='" + quality + '\'' +
                ", path='" + getPath() + '\'' +
                ", size=" + size +
                '}';
    }
}
