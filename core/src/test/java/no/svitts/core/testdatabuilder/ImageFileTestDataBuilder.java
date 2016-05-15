package no.svitts.core.testdatabuilder;

import no.svitts.core.file.ImageFile;
import no.svitts.core.id.Id;

public class ImageFileTestDataBuilder implements TestDataBuilder<ImageFile> {

    private String id;
    private String path;

    public ImageFileTestDataBuilder() {
        id = Id.get();
        path = "path";
    }

    @Override
    public ImageFile build() {
        return new ImageFile(id, path);
    }

    public ImageFileTestDataBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ImageFileTestDataBuilder path(String path) {
        this.path = path;
        return this;
    }

}
