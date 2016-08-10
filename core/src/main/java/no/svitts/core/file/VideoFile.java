package no.svitts.core.file;

import no.svitts.core.constraint.Length;
import no.svitts.core.constraint.NonNegative;
import no.svitts.core.constraint.NotNullOrEmpty;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.File;

@Embeddable
public class VideoFile {

    private static final int PATH_MAX_LENGTH = 255;
    private static final int NAME_MAX_LENGTH = 255;

    @Transient
    private File file;

    private VideoFile() {
    }

    public VideoFile(String path) {
        file = new File(path);
    }

    @Override
    public String toString() {
        return "VideoFile{" +
                "path='" + getPath() + '\'' +
                ", name='" + getName() + '\'' +
                ", size='" + getSize() +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && getClass() == object.getClass()) {
            VideoFile videoFile = (VideoFile) object;
            return getPath().equals(videoFile.getPath())
                    && getName().equals(videoFile.getName())
                    && getSize() == videoFile.getSize();
        } else {
            return false;
        }
    }

    @NotNullOrEmpty
    @Length(length = PATH_MAX_LENGTH)
    public String getPath() {
        return file.getPath();
    }

    private void setPath(String path) {
        file = new File(path);
    }

    @NotNullOrEmpty
    @Length(length = NAME_MAX_LENGTH)
    @Transient
    public String getName() {
        return file.getName();
    }

    @NonNegative
    @Transient
    public long getSize() {
        return file.length();
    }

}
