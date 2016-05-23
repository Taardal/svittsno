package no.svitts.core.file;

import java.io.File;

public class ImageFile extends File {

    private final String id;
    private ImageType imageType;

    public ImageFile(String id, String path, ImageType imageType) {
        super(path);
        this.id = id;
        this.imageType = imageType;
    }

    public String getId() {
        return id;
    }

    public ImageType getImageType() {
        return imageType;
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
