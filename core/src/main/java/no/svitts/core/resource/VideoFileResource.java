package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.file.VideoFile;
import no.svitts.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api
@Path("/videofile")
@Produces(MediaType.APPLICATION_JSON)
public class VideoFileResource extends CoreResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoFileResource.class);
    private Repository<VideoFile> videoFileRepository;

    public VideoFileResource(Repository<VideoFile> videoFileRepository) {
        this.videoFileRepository = videoFileRepository;
    }

    @ApiOperation(value = "Get all video files.", notes = "Lists all the video files stored in the database", response = Response.class)
    @GET
    @Path("/all")
    public String getAllVideoFiles() {
        LOGGER.info("Received request to GET all video files");
        return gson.toJson(videoFileRepository.getAll());
    }

    @GET
    @Path("/{id}")
    public String getVideoFileById(@PathParam("id") String id) {
        LOGGER.info("Received request to GET video file with ID [{}]", id);
        return gson.toJson(videoFileRepository.getById(id));
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createVideoFile(String json) {
        LOGGER.info("Received request to POST video file by json [{}]", json);
        VideoFile videoFile = gson.fromJson(json, VideoFile.class);
        return getResponse(videoFileRepository.insert(videoFile));
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateVideoFile(@PathParam("id") String id, String json) {
        LOGGER.info("Received request to PUT video file by json [{}]", json);
        VideoFile videoFile = gson.fromJson(json, VideoFile.class);
        return getResponse(videoFileRepository.update(videoFile));
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteVideoFile(@PathParam("id") String id) {
        LOGGER.info("Received request to DELETE video file by ID [{}]", id);
        return getResponse(videoFileRepository.delete(id));
    }
}