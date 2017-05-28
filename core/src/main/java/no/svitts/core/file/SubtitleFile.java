package no.svitts.core.file;

import no.svitts.core.constraint.Length;
import no.svitts.core.constraint.NonNegative;
import no.svitts.core.constraint.NotNullOrEmpty;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.File;

@Embeddable
public class SubtitleFile {

    public static final String[] SUBTITLE_FORMATS = {"ssa", "sub","rt", "aqt", "ssf", "mpl", "usf", "ttxt", "jss", "dks", "smi", "cvd", "svcd", "srt", "pjs", "idx", "psb"};
    public static final int PATH_MAX_LENGTH = 255;
    public static final int NAME_MAX_LENGTH = 255;
    public static final int LANGUAGE_MAX_LENGTH = 255;

    @Transient
    private File file;
    
    private String language;

    private SubtitleFile() {
    }

    public SubtitleFile(String path, String language) {
        file = new File(path);
        this.language = language;
    }

    @Override
    public String toString() {
        return "SubtitleFile{" +
                "path='" + getPath() + '\'' +
                ", name='" + getName() + '\'' +
                ", size='" + getSize() + '\'' +
                ", language='" + getLanguage() +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && getClass() == object.getClass()) {
            SubtitleFile subtitleFile = (SubtitleFile) object;
            return getPath().equals(subtitleFile.getPath())
                    && getName().equals(subtitleFile.getName())
                    && getSize() == subtitleFile.getSize()
                    && getLanguage().equals(subtitleFile.getLanguage());
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

    @Length(length = LANGUAGE_MAX_LENGTH)
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
