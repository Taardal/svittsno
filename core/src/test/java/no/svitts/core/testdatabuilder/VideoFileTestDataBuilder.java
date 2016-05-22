package no.svitts.core.testdatabuilder;

import no.svitts.core.file.VideoFile;
import no.svitts.core.id.Id;

public class VideoFileTestDataBuilder implements TestDataBuilder<VideoFile> {

    private String id;
    private String path;

    public VideoFileTestDataBuilder() {
        id = Id.get();
        path = "path";
    }

    @Override
    public VideoFile build() {
        return new VideoFile(id, path);
    }

    public VideoFileTestDataBuilder id(String id) {
        this.id = id;
        return this;
    }

    public VideoFileTestDataBuilder path(String path) {
        this.path = path;
        return this;
    }

    public VideoFileTestDataBuilder sherlockHolmes() {
        id = Id.get();
        path = "\\\\\\\\TAARDAL-SERVER\\\\Misc\\\\svitts\\\\sherlock_holmes\\\\sherlock_holmes.mkv";
        return this;
    }

    public VideoFileTestDataBuilder sherlockHolmesAGameOfShadows() {
        id = Id.get();
        path = "\\\\\\\\TAARDAL-SERVER\\\\Misc\\\\svitts\\\\sherlock_holmes_a_game_of_shadows\\\\sherlock_holmes_a_game_of_shadows.mkv";
        return this;
    }
}
