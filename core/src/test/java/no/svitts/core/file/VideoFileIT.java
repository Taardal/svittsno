package no.svitts.core.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.CoreJerseyTest;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.gson.serializer.VideoFileSerializer;
import no.svitts.core.id.Id;
import no.svitts.core.repository.VideoFileRepository;
import no.svitts.core.resource.VideoFileResource;
import no.svitts.core.testdatabuilder.VideoFileTestDataBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class VideoFileIT extends CoreJerseyTest {

    private static final String VIDEO_FILE_RESOURCE = "videofile";
    private static final int ID_MAX_LENGTH = 255;
    private static final int PATH_MAX_LENGTH = 255;

    private DataSource dataSource;
    private Gson gson;
    private VideoFileTestDataBuilder videoFileTestDataBuilder;

    @Override
    public Application configure() {
        dataSource = getDataSource();
        return getResourceConfig(new VideoFileResource(new VideoFileRepository(dataSource)));
    }

    @Before
    public void setUp() throws Exception {
        gson = getGson();
        videoFileTestDataBuilder = new VideoFileTestDataBuilder();
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
        super.tearDown();
    }

    @Test
    public void getVideoFileById_VideoFileDoesNotExist_ShouldReturnUnknownVideoFile() {
        Response response = target(VIDEO_FILE_RESOURCE).path(Id.get()).request().get();
        VideoFile videoFile = gson.fromJson(response.readEntity(String.class), VideoFile.class);
        assertEquals(VideoFileRepository.UNKNOWN_VIDEO_FILE_ID, videoFile.getId());
        response.close();
    }

    @Test
    public void createVideoFile_IdTooLong_ShouldAbortAndReturnServerError() {
        VideoFile videoFile = videoFileTestDataBuilder.id(getRandomString(ID_MAX_LENGTH + 1)).build();
        Entity<String> videoFileJsonEntity = Entity.entity(gson.toJson(videoFile), MediaType.APPLICATION_JSON);

        Response response = target(VIDEO_FILE_RESOURCE).path("create").request().post(videoFileJsonEntity);

        assertEquals(500, response.getStatus());
        assertVideoFileDoesNotExist(videoFile);
        response.close();
    }

    @Test
    public void createVideoFile_PathTooLong_ShouldAbortAndReturnServerError() {
        VideoFile videoFile = videoFileTestDataBuilder.path(getRandomString(PATH_MAX_LENGTH + 1)).build();
        Entity<String> videoFileJsonEntity = Entity.entity(gson.toJson(videoFile), MediaType.APPLICATION_JSON);

        Response response = target(VIDEO_FILE_RESOURCE).path("create").request().post(videoFileJsonEntity);

        assertEquals(500, response.getStatus());
        assertVideoFileDoesNotExist(videoFile);
        response.close();
    }

    @Test
    public void createVideoFile_StringFieldsMaximumLength_ShouldReturnOk() {
        VideoFile videoFile = videoFileTestDataBuilder.id(getRandomString(ID_MAX_LENGTH)).path(getRandomString(PATH_MAX_LENGTH)).build();
        Entity<String> videoFileJsonEntity = Entity.entity(gson.toJson(videoFile), MediaType.APPLICATION_JSON);

        Response response = target(VIDEO_FILE_RESOURCE).path("create").request().post(videoFileJsonEntity);

        assertEquals(200, response.getStatus());
        deleteVideoFile(videoFile);
        response.close();
    }

    @Test
    public void updateVideoFile_PathTooLong_ShouldAbortAndReturnServerError() {
        VideoFile videoFile = videoFileTestDataBuilder.build();
        createVideoFile(videoFile);
        VideoFile videoFileWithTooLongPath = videoFileTestDataBuilder.id(videoFile.getId()).path(getRandomString(ID_MAX_LENGTH + 1)).build();
        Entity<String> videoFileWithTooLongPathJsonEntity = Entity.entity(gson.toJson(videoFileWithTooLongPath), MediaType.APPLICATION_JSON);

        Response response = target(VIDEO_FILE_RESOURCE).path("update").path(videoFile.getId()).request().put(videoFileWithTooLongPathJsonEntity);

        assertEquals(500, response.getStatus());
        response.close();
    }

    @Test
    public void deleteVideoFile_VideoFileDoesNotExist_ShouldAbortAndReturnServerError() {
        Response response = target(VIDEO_FILE_RESOURCE).path("delete").path(Id.get()).request().delete();
        assertEquals(500, response.getStatus());
        response.close();
    }

    private void createVideoFile(VideoFile videoFile) {
        Entity<String> videoFileJsonEntity = Entity.entity(gson.toJson(videoFile), MediaType.APPLICATION_JSON);
        Response response = target(VIDEO_FILE_RESOURCE).path("create").request().post(videoFileJsonEntity);
        assertEquals(200, response.getStatus());
        assertVideoFileExists(videoFile);
        response.close();
    }

    private void assertVideoFileExists(VideoFile videoFile) {
        Response response = target(VIDEO_FILE_RESOURCE).path(videoFile.getId()).request().get();
        assertVideoFile(videoFile, gson.fromJson(response.readEntity(String.class), VideoFile.class));
        response.close();
    }

    private void assertVideoFile(VideoFile expectedVideoFile, VideoFile actualVideoFile) {
        assertEquals(expectedVideoFile.getId(), actualVideoFile.getId());
        assertEquals(expectedVideoFile.getPath(), actualVideoFile.getPath());
    }

    private void deleteVideoFile(VideoFile videoFile) {
        Response response = target(VIDEO_FILE_RESOURCE).path("delete").path(videoFile.getId()).request().delete();
        assertEquals(200, response.getStatus());
        assertVideoFileDoesNotExist(videoFile);
        response.close();
    }

    private void assertVideoFileDoesNotExist(VideoFile videoFile) {
        Response response = target(VIDEO_FILE_RESOURCE).path(videoFile.getId()).request().get();
        VideoFile videoFileFromResource = gson.fromJson(response.readEntity(String.class), VideoFile.class);
        assertEquals(VideoFileRepository.UNKNOWN_VIDEO_FILE_ID, videoFileFromResource.getId());
        response.close();
    }

    @Override
    protected Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(VideoFile.class, new VideoFileSerializer()).create();
    }
}
