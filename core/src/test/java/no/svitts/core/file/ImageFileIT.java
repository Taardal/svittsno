package no.svitts.core.file;

import com.google.gson.Gson;
import no.svitts.core.CoreJerseyTest;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.id.Id;
import no.svitts.core.repository.ImageFileRepository;
import no.svitts.core.resource.ImageFileResource;
import no.svitts.core.testdatabuilder.ImageFileTestDataBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class ImageFileIT extends CoreJerseyTest {

    private static final String IMAGE_FILE_RESOURCE = "imagefile";
    private static final int ID_MAX_LENGTH = 255;
    private static final int PATH_MAX_LENGTH = 255;

    private DataSource dataSource;
    private Gson gson;
    private ImageFileTestDataBuilder imageFileTestDataBuilder;

    @Override
    public Application configure() {
        dataSource = getDataSource();
        return getResourceConfig(new ImageFileResource(new ImageFileRepository(dataSource)));
    }

    @Before
    public void setUp() throws Exception {
        gson = getGson();
        imageFileTestDataBuilder = new ImageFileTestDataBuilder();
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
        super.tearDown();
    }

    @Test
    public void getImageFileById_ImageFileDoesNotExist_ShouldReturnUnknownImageFile() {
        Response response = target(IMAGE_FILE_RESOURCE).path(Id.get()).request().get();
        ImageFile imageFile = gson.fromJson(response.readEntity(String.class), ImageFile.class);
        assertEquals(ImageFileRepository.UNKNOWN_IMAGE_FILE_ID, imageFile.getId());
        response.close();
    }

    @Test
    public void createImageFile_IdTooLong_ShouldAbortAndReturnServerError() {
        ImageFile imageFile = imageFileTestDataBuilder.id(getRandomString(ID_MAX_LENGTH + 1)).build();
        Entity<String> imageFileJsonEntity = Entity.entity(gson.toJson(imageFile), MediaType.APPLICATION_JSON);

        Response response = target(IMAGE_FILE_RESOURCE).path("create").request().post(imageFileJsonEntity);

        assertEquals(500, response.getStatus());
        assertImageFileDoesNotExist(imageFile);
        response.close();
    }

    @Test
    public void createImageFile_PathTooLong_ShouldAbortAndReturnServerError() {
        ImageFile imageFile = imageFileTestDataBuilder.path(getRandomString(PATH_MAX_LENGTH + 1)).build();
        Entity<String> imageFileJsonEntity = Entity.entity(gson.toJson(imageFile), MediaType.APPLICATION_JSON);

        Response response = target(IMAGE_FILE_RESOURCE).path("create").request().post(imageFileJsonEntity);

        assertEquals(500, response.getStatus());
        assertImageFileDoesNotExist(imageFile);
        response.close();
    }

    @Test
    public void createImageFile_StringFieldsMaximumLength_ShouldReturnOk() {
        ImageFile imageFile = imageFileTestDataBuilder.id(getRandomString(ID_MAX_LENGTH)).path(getRandomString(PATH_MAX_LENGTH)).build();
        Entity<String> imageFileJsonEntity = Entity.entity(gson.toJson(imageFile), MediaType.APPLICATION_JSON);

        Response response = target(IMAGE_FILE_RESOURCE).path("create").request().post(imageFileJsonEntity);

        assertEquals(200, response.getStatus());
        deleteImageFile(imageFile);
        response.close();
    }

    @Test
    public void updateImageFile_PathTooLong_ShouldAbortAndReturnServerError() {
        ImageFile imageFile = imageFileTestDataBuilder.build();
        createImageFile(imageFile);
        ImageFile imageFileWithTooLongPath = imageFileTestDataBuilder.id(imageFile.getId()).path(getRandomString(ID_MAX_LENGTH + 1)).build();
        Entity<String> imageFileWithTooLongPathJsonEntity = Entity.entity(gson.toJson(imageFileWithTooLongPath), MediaType.APPLICATION_JSON);

        Response response = target(IMAGE_FILE_RESOURCE).path("update").path(imageFile.getId()).request().put(imageFileWithTooLongPathJsonEntity);

        assertEquals(500, response.getStatus());
        response.close();
    }

    @Test
    public void deleteImageFile_ImageFileDoesNotExist_ShouldAbortAndReturnServerError() {
        Response response = target(IMAGE_FILE_RESOURCE).path("delete").path(Id.get()).request().delete();
        assertEquals(500, response.getStatus());
        response.close();
    }

    private void createImageFile(ImageFile imageFile) {
        Entity<String> imageFileJsonEntity = Entity.entity(gson.toJson(imageFile), MediaType.APPLICATION_JSON);
        Response response = target(IMAGE_FILE_RESOURCE).path("create").request().post(imageFileJsonEntity);
        assertEquals(200, response.getStatus());
        assertImageFileExists(imageFile);
        response.close();
    }

    private void assertImageFileExists(ImageFile imageFile) {
        Response response = target(IMAGE_FILE_RESOURCE).path(imageFile.getId()).request().get();
        assertImageFile(imageFile, gson.fromJson(response.readEntity(String.class), ImageFile.class));
        response.close();
    }

    private void assertImageFile(ImageFile expectedImageFile, ImageFile actualImageFile) {
        assertEquals(expectedImageFile.getId(), actualImageFile.getId());
        assertEquals(expectedImageFile.getPath(), actualImageFile.getPath());
    }

    private void deleteImageFile(ImageFile imageFile) {
        Response response = target(IMAGE_FILE_RESOURCE).path("delete").path(imageFile.getId()).request().delete();
        assertEquals(200, response.getStatus());
        assertImageFileDoesNotExist(imageFile);
        response.close();
    }

    private void assertImageFileDoesNotExist(ImageFile imageFile) {
        Response response = target(IMAGE_FILE_RESOURCE).path(imageFile.getId()).request().get();
        ImageFile imageFileFromResource = gson.fromJson(response.readEntity(String.class), ImageFile.class);
        assertEquals(ImageFileRepository.UNKNOWN_IMAGE_FILE_ID, imageFileFromResource.getId());
        response.close();
    }


    @Override
    protected Gson getGson() {
        return new Gson();
    }
    
}
