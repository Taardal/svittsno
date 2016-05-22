package no.svitts.core.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.CoreJerseyTest;
import no.svitts.core.file.VideoFile;
import no.svitts.core.gson.serializer.VideoFileSerializer;
import no.svitts.core.repository.Repository;
import no.svitts.core.repository.VideoFileRepository;
import no.svitts.core.testdatabuilder.VideoFileTestDataBuilder;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class VideoFileResourceTest extends CoreJerseyTest {

    private static final String VIDEO_FILE_RESOURCE = "videofile";

    private Gson gson;
    private VideoFileTestDataBuilder videoFileTestDataBuilder;
    private Repository<VideoFile> mockVideoFileRepository;

    @Override
    protected Application configure() {
        mockVideoFileRepository =  mock(VideoFileRepository.class);
        return getResourceConfig(new VideoFileResource(mockVideoFileRepository));
    }

    @Override
    @Before
    public void setUp() throws Exception {
        gson = getGson();
        videoFileTestDataBuilder = new VideoFileTestDataBuilder();
        super.setUp();
    }

    @Test
    public void getVideoFileById_ShouldReturnExpectedVideoFileAsJson() {
        VideoFile videoFile = videoFileTestDataBuilder.build();
        when(mockVideoFileRepository.getById(videoFile.getId())).thenReturn(videoFile);
        String expectedJson = gson.toJson(videoFile);

        String json = target(VIDEO_FILE_RESOURCE).path(videoFile.getId()).request().get(String.class);

        assertEquals(expectedJson, json);
        verify(mockVideoFileRepository, times(1)).getById(videoFile.getId());
    }

    @Test
    public void createVideoFile_ShouldReturnOk() {
        VideoFile videoFile = videoFileTestDataBuilder.build();
        when(mockVideoFileRepository.insert(any(VideoFile.class))).thenReturn(true);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(videoFile), MediaType.APPLICATION_JSON);

        Response response = target(VIDEO_FILE_RESOURCE).path("create").request().post(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(mockVideoFileRepository, times(1)).insert(any(VideoFile.class));
    }

    @Test
    public void createVideoFile_Fails_ShouldReturnServerError() {
        VideoFile videoFile = videoFileTestDataBuilder.build();
        when(mockVideoFileRepository.insert(any(VideoFile.class))).thenReturn(false);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(videoFile), MediaType.APPLICATION_JSON);

        Response response = target(VIDEO_FILE_RESOURCE).path("create").request().post(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(mockVideoFileRepository, times(1)).insert(any(VideoFile.class));
    }

    @Test
    public void updateVideoFile_ShouldReturnOk() {
        VideoFile videoFile = videoFileTestDataBuilder.build();
        when(mockVideoFileRepository.update(any(VideoFile.class))).thenReturn(true);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(videoFile), MediaType.APPLICATION_JSON);

        Response response = target(VIDEO_FILE_RESOURCE).path("update").path(videoFile.getId()).request().put(jsonEntity, Response.class);

        assertEquals(200, response.getStatus());
        verify(mockVideoFileRepository, times(1)).update(any(VideoFile.class));
    }

    @Test
    public void updateVideoFile_Fails_ShouldReturnServerError() {
        VideoFile videoFile = videoFileTestDataBuilder.build();
        when(mockVideoFileRepository.update(any(VideoFile.class))).thenReturn(false);
        Entity<String> jsonEntity = Entity.entity(gson.toJson(videoFile), MediaType.APPLICATION_JSON);

        Response response = target(VIDEO_FILE_RESOURCE).path("update").path(videoFile.getId()).request().put(jsonEntity, Response.class);

        assertEquals(500, response.getStatus());
        verify(mockVideoFileRepository, times(1)).update(any(VideoFile.class));
    }

    @Test
    public void deleteVideoFile_ShouldReturnOk() {
        VideoFile videoFile = videoFileTestDataBuilder.build();
        when(mockVideoFileRepository.delete(videoFile.getId())).thenReturn(true);

        Response response = target(VIDEO_FILE_RESOURCE).path("delete").path(videoFile.getId()).request().delete();

        assertEquals(200, response.getStatus());
        verify(mockVideoFileRepository, times(1)).delete(videoFile.getId());
    }

    @Test
    public void deleteVideoFile_Fails_ShouldReturnServerError() {
        VideoFile videoFile = videoFileTestDataBuilder.build();
        when(mockVideoFileRepository.delete(videoFile.getId())).thenReturn(false);

        Response response = target(VIDEO_FILE_RESOURCE).path("delete").path(videoFile.getId()).request().delete();

        assertEquals(500, response.getStatus());
        verify(mockVideoFileRepository, times(1)).delete(videoFile.getId());
    }

    @Override
    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(VideoFile.class, new VideoFileSerializer())
                .create();
    }

}
