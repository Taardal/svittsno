package no.svitts.core.resource;

import com.google.gson.Gson;
import no.svitts.core.CoreJerseyTest;
import no.svitts.core.file.ImageFile;
import no.svitts.core.repository.ImageFileRepository;
import no.svitts.core.repository.Repository;
import no.svitts.core.testdatabuilder.ImageFileTestDataBuilder;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ImageFileResourceTest extends CoreJerseyTest {

    private static final String IMAGE_FILE_RESOURCE = "imagefile";

    private Gson gson;
    private ImageFileTestDataBuilder imageFileTestDataBuilder;
    private Repository<ImageFile> mockImageFileRepository;

    @Override
    protected Application configure() {
        mockImageFileRepository =  mock(ImageFileRepository.class);
        return getResourceConfig(new ImageFileResource(mockImageFileRepository));
    }

    @Override
    @Before
    public void setUp() throws Exception {
        gson = getGson();
        imageFileTestDataBuilder = new ImageFileTestDataBuilder();
        super.setUp();
    }

    @Test
    public void getImageFileById_ShouldReturnExpectedImageFileAsJson() {
        ImageFile imageFile = imageFileTestDataBuilder.build();
        when(mockImageFileRepository.getById(imageFile.getId())).thenReturn(imageFile);
        String expectedJson = gson.toJson(imageFile);

        String json = target(IMAGE_FILE_RESOURCE).path(imageFile.getId()).request().get(String.class);

        assertEquals(expectedJson, json);
        verify(mockImageFileRepository, times(1)).getById(imageFile.getId());
    }

    @Test
    public void createImageFile_ShouldReturnOk() {
        ImageFile imageFile = imageFileTestDataBuilder.build();
        when(mockImageFileRepository.insert(any(ImageFile.class))).thenReturn("");
        Entity<String> jsonEntity = Entity.entity(gson.toJson(imageFile), MediaType.APPLICATION_JSON);

        Response response = target(IMAGE_FILE_RESOURCE).path("create").request().post(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(mockImageFileRepository, times(1)).insert(any(ImageFile.class));
    }

    @Test
    public void createImageFile_Fails_ShouldReturnServerError() {
        ImageFile imageFile = imageFileTestDataBuilder.build();
        when(mockImageFileRepository.insert(any(ImageFile.class))).thenReturn("");
        Entity<String> jsonEntity = Entity.entity(gson.toJson(imageFile), MediaType.APPLICATION_JSON);

        Response response = target(IMAGE_FILE_RESOURCE).path("create").request().post(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(mockImageFileRepository, times(1)).insert(any(ImageFile.class));
    }

    @Test
    public void updateImageFile_ShouldReturnOk() {
        ImageFile imageFile = imageFileTestDataBuilder.build();
        when(mockImageFileRepository.update(any(ImageFile.class))).thenReturn(true);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(imageFile), MediaType.APPLICATION_JSON);

        Response response = target(IMAGE_FILE_RESOURCE).path("update").path(imageFile.getId()).request().put(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(mockImageFileRepository, times(1)).update(any(ImageFile.class));
    }

    @Test
    public void updateImageFile_Fails_ShouldReturnServerError() {
        ImageFile imageFile = imageFileTestDataBuilder.build();
        when(mockImageFileRepository.update(any(ImageFile.class))).thenReturn(false);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(imageFile), MediaType.APPLICATION_JSON);

        Response response = target(IMAGE_FILE_RESOURCE).path("update").path(imageFile.getId()).request().put(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(mockImageFileRepository, times(1)).update(any(ImageFile.class));
    }

    @Test
    public void deleteImageFile_ShouldReturnOk() {
        ImageFile imageFile = imageFileTestDataBuilder.build();
        when(mockImageFileRepository.delete(imageFile.getId())).thenReturn(true);

        Response response = target(IMAGE_FILE_RESOURCE).path("delete").path(imageFile.getId()).request().delete();

        assertEquals(200, response.getStatus());
        verify(mockImageFileRepository, times(1)).delete(imageFile.getId());
    }

    @Test
    public void deleteImageFile_Fails_ShouldReturnServerError() {
        ImageFile imageFile = imageFileTestDataBuilder.build();
        when(mockImageFileRepository.delete(imageFile.getId())).thenReturn(false);

        Response response = target(IMAGE_FILE_RESOURCE).path("delete").path(imageFile.getId()).request().delete();

        assertEquals(500, response.getStatus());
        verify(mockImageFileRepository, times(1)).delete(imageFile.getId());
    }


    @Override
    protected Gson getGson() {
        return new Gson();
    }
}
