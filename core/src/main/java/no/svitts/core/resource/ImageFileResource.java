package no.svitts.core.resource;

import io.swagger.annotations.Api;
import no.svitts.core.criteria.SearchCriteria;
import no.svitts.core.criteria.SearchKey;
import no.svitts.core.file.ImageFile;
import no.svitts.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("imagefile")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "ImageFileResource")
public class ImageFileResource extends CoreResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageFileResource.class);

    private Repository<ImageFile> imageFileRepository;

    public ImageFileResource(Repository<ImageFile> imageFileRepository) {
        this.imageFileRepository = imageFileRepository;
    }

    @GET
    @Path("{id}")
    public Response getImageFileById(@PathParam("id") String id) {
        LOGGER.info("Received request to GET image file with ID [{}]", id);
        String json = gson.toJson(imageFileRepository.getById(id));
        return Response.ok().entity(json).build();
    }

    @GET
    @Path("name")
    public Response getImageFilesByName(@QueryParam("name") String name, @QueryParam("limit") int limit) {
        LOGGER.info("Received request to GET max [{}] image file(s) with name [{}]", limit, name);
        List<ImageFile> imageFiles = imageFileRepository.search(new SearchCriteria(SearchKey.NAME, name, limit));
        return Response.ok().entity(gson.toJson(imageFiles)).build();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createImageFile(String json) {
        LOGGER.info("Received request to POST image file by json [{}]", json);
        ImageFile imageFile = gson.fromJson(json, ImageFile.class);
        return getResponse(imageFileRepository.insert(imageFile));
    }

    @PUT
    @Path("update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateImageFile(@PathParam("id") String id, String json) {
        LOGGER.info("Received request to PUT image file by json [{}]", json);
        ImageFile imageFile = gson.fromJson(json, ImageFile.class);
        return getResponse(imageFileRepository.update(imageFile));
    }

    @DELETE
    @Path("delete/{id}")
    public Response deleteImageFile(@PathParam("id") String id) {
        LOGGER.info("Received request to DELETE image file by ID [{}]", id);
        return getResponse(imageFileRepository.delete(id));
    }

}
