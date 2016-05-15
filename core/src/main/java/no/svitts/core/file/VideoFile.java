package no.svitts.core.file;

import java.io.File;
import java.text.DecimalFormat;


public class VideoFile extends File {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private final String id;

    public VideoFile(String id, String path) {
        super(path);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public long getSize() {
        return length();
    }

    @Override
    public String toString() {
        return "VideoFile{" +
                "id='" + id + '\'' +
                ", name='" + getName() + '\'' +
                ", path='" + getPath() + '\'' +
                ", size=" + getSize() +
                '}';
    }

    private String getPrettySize() {
        double bytes = length();
        double kilobytes = bytes / 1024;
        double megabytes = kilobytes / 1024;
        double gigabytes = megabytes / 1024;
        if (gigabytes > 1) {
            return DECIMAL_FORMAT.format(gigabytes) + " GB";
        } else if (megabytes > 1) {
            return DECIMAL_FORMAT.format(megabytes) + " MB";
        } else if (kilobytes > 1) {
            return DECIMAL_FORMAT.format(kilobytes) + " KB";
        } else {
            return DECIMAL_FORMAT.format(bytes) + " B";
        }
    }

}
