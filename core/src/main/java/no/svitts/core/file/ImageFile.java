package no.svitts.core.file;

import java.io.File;

public class ImageFile extends File {

    private final String id;
    private String movieId;
    private String name;
    private String format;

    public ImageFile(String pathname, String id) {
        super(pathname);
        this.id = id;
    }
}
