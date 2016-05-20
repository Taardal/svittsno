package no.svitts.core.resource;

import io.swagger.annotations.Api;
import no.svitts.core.criteria.SearchCriteria;
import no.svitts.core.criteria.SearchKey;
import no.svitts.core.file.VideoFile;
import no.svitts.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api
@Path("videofile")
@Produces(MediaType.APPLICATION_JSON)
public class VideoFileResource extends CoreResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoFileResource.class);

    private Repository<VideoFile> videoFileRepository;

    public VideoFileResource(Repository<VideoFile> videoFileRepository) {
        this.videoFileRepository = videoFileRepository;
    }

    @GET
    @Path("{id}")
    public Response getVideoFileById(@PathParam("id") String id) {
        LOGGER.info("Received request to GET video file with ID [{}]", id);
        return Response.ok().entity(gson.toJson(videoFileRepository.getById(id))).build();
    }

    @GET
    @Path("name")
    public Response getVideoFilesByName(@QueryParam("name") String name, @QueryParam("limit") int limit) {
        LOGGER.info("Received request to GET max [{}] video file(s) with name [{}]", limit, name);
        List<VideoFile> videoFiles = videoFileRepository.search(new SearchCriteria(SearchKey.NAME, name, limit));
        return Response.ok().entity(gson.toJson(videoFiles)).build();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createVideoFile(String json) {
        LOGGER.info("Received request to POST video file by json [{}]", json);
        VideoFile videoFile = gson.fromJson(json, VideoFile.class);
        return getResponse(videoFileRepository.insert(videoFile));
    }

    @PUT
    @Path("update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateVideoFile(@PathParam("id") String id, String json) {
        LOGGER.info("Received request to PUT video file by json [{}]", json);
        VideoFile videoFile = gson.fromJson(json, VideoFile.class);
        return getResponse(videoFileRepository.update(videoFile));
    }

    @DELETE
    @Path("delete/{id}")
    public Response deleteVideoFile(@PathParam("id") String id) {
        LOGGER.info("Received request to DELETE video file by ID [{}]", id);
        return getResponse(videoFileRepository.delete(id));
    }
}