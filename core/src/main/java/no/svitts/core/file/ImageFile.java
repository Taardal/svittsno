package no.svitts.core.file;

import java.io.File;

public class ImageFile extends File {

    private final String id;

    public ImageFile(String id, String path) {
        super(path);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "VideoFile{" +
                "id='" + id + '\'' +
                ", name='" + getName() + '\'' +
                ", path='" + getPath() + '\'' +
                '}';
    }
}
