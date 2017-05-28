package no.svitts.core.file;

import no.svitts.core.constraint.Length;
import no.svitts.core.constraint.NonNegative;
import no.svitts.core.constraint.NotNullOrEmpty;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.File;

@Embeddable
public class VideoFile {


    public static final String[] VIDEO_FORMATS = {"gif", "vob", "amv", "ogg", "nsv", "rmvb", "f4a", "drc", "avi", "mov", "roq", "f4b", "mkv", "mxf", "yuv", "3gp", "3g2", "ogv", "wmv"," qt", "mpe", "f4p", "mpg", "mng", "svi", "m4p", "f4v", "webm", "m4v", "mp2", "mp4", "m2v", "flv", "mpv", "asf", "mpeg", "rm"};
    public static final int PATH_MAX_LENGTH = 255;
    public static final int NAME_MAX_LENGTH = 255;
    public static final int VIDEO_FORMAT_MAX_LENGTH = 255;
    public static final int AUDIO_FORMAT_MAX_LENGTH = 255;

    @Transient
    private File file;

    private String videoFormat;
    private String audioFormat;

    private VideoFile() {
    }

    public VideoFile(String path) {
        this(path, "", "");
    }

    public VideoFile(String path, String videoFormat, String audioFormat) {
        file = new File(path);
        this.videoFormat = videoFormat;
        this.audioFormat = audioFormat;
    }

    @Override
    public String toString() {
        return "VideoFile{" +
                "path='" + getPath() + '\'' +
                ", name='" + getName() + '\'' +
                ", size='" + getSize() + '\'' +
                ", videoFormat='" + getVideoFormat() + '\'' +
                ", audioFormat='" + getAudioFormat() +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && getClass() == object.getClass()) {
            VideoFile videoFile = (VideoFile) object;
            return getPath().equals(videoFile.getPath())
                    && getName().equals(videoFile.getName())
                    && getSize() == videoFile.getSize()
                    && (getVideoFormat() == null && videoFile.getVideoFormat() == null) || getVideoFormat().equals(videoFile.getVideoFormat())
                    && (getAudioFormat() == null && videoFile.getAudioFormat() == null) || getAudioFormat().equals(videoFile.getAudioFormat());
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

    @Length(length = VIDEO_FORMAT_MAX_LENGTH)
    public String getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }

    @Length(length = AUDIO_FORMAT_MAX_LENGTH)
    public String getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(String audioFormat) {
        this.audioFormat = audioFormat;
    }
}
