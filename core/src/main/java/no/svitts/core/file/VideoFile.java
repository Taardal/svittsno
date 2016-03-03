package no.svitts.core.file;

import java.io.File;

public class VideoFile extends File {

    protected final int movieId;
    protected String name;
    protected String format;
    protected int directoryId;
    protected int size;

    public VideoFile(String pathname, int movieId, String name, String format, int directoryId, int size) {
        super(pathname);
        this.movieId = movieId;
        this.name = name;
        this.format = format;
        this.directoryId = directoryId;
        this.size = size;
    }

    public int getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(int directoryId) {
        this.directoryId = directoryId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMovieId() {
        return movieId;
    }

    @Override
    public String toString() {
        return "VideoFile{" +
                "movieId=" + movieId +
                ", name='" + name + '\'' +
                ", format='" + format + '\'' +
                ", path='" + getPath() + '\'' +
                ", directoryId=" + directoryId +
                ", size=" + size +
                '}';
    }
}
