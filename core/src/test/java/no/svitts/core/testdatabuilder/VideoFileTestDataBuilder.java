package no.svitts.core.testdatabuilder;

import no.svitts.core.file.VideoFile;
import no.svitts.core.id.Id;

import java.io.File;
import java.io.IOException;

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

    public VideoFileTestDataBuilder tempFile() throws IOException {
        path = File.createTempFile("temp", ".txt").getPath();
        return this;
    }

    public VideoFileTestDataBuilder gerisGame() {
        id = Id.get();
        path = "\\\\TAARDAL-SERVER\\svitts\\geris game\\gerisgame.mkv";
        return this;
    }

    public VideoFileTestDataBuilder forTheBirds() {
        id = Id.get();
        path = "\\\\TAARDAL-SERVER\\svitts\\for the birds\\forthebirds.mkv";
        return this;
    }

    public VideoFileTestDataBuilder sherlockHolmes() {
        id = Id.get();
        path = "\\\\TAARDAL-SERVER\\svitts\\Sherlock Holmes 2009 1080p BluRay x264-SECTOR7\\s7-sherlock.holmes.1080p.x264.mkv";
        return this;
    }

    public VideoFileTestDataBuilder sherlockHolmesAGameOfShadows() {
        id = Id.get();
        path = "\\\\TAARDAL-SERVER\\svitts\\Sherlock.Holmes.A.Game.Of.Shadows.2011.1080p.BluRay.x264-RSG [PublicHD]\\rsg-holmes2-1080p.mkv";
        return this;
    }
}
