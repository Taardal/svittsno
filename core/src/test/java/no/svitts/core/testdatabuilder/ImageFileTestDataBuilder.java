package no.svitts.core.testdatabuilder;

import no.svitts.core.file.ImageFile;
import no.svitts.core.file.ImageType;
import no.svitts.core.id.Id;

public class ImageFileTestDataBuilder implements TestDataBuilder<ImageFile> {

    private String id;
    private String path;
    private ImageType imageType;

    public ImageFileTestDataBuilder() {
        id = Id.get();
        path = "path";
        imageType = ImageType.POSTER;
    }

    @Override
    public ImageFile build() {
        return new ImageFile(id, path, imageType);
    }

    public ImageFileTestDataBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ImageFileTestDataBuilder path(String path) {
        this.path = path;
        return this;
    }

    public ImageFileTestDataBuilder imageType(ImageType imageType) {
        this.imageType = imageType;
        return this;
    }

}
