package no.svitts.core.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.svitts.core.file.ImageFile;
import no.svitts.core.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "ImageFileResource")
@Path("imagefile")
@Produces(MediaType.APPLICATION_JSON)
public class ImageFileResource extends CoreResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageFileResource.class);
    private Repository<ImageFile> imageFileRepository;

    public ImageFileResource(Repository<ImageFile> imageFileRepository) {
        this.imageFileRepository = imageFileRepository;
    }

    @ApiOperation(value = "Get all image files.", notes = "Lists all the image files stored in the database as JSON", response = Response.class)
    @GET
    @Path("all")
    public String getAllImageFiles() {
        LOGGER.info("Received request to GET all image files");
        return gson.toJson(imageFileRepository.getAll());
    }

    @GET
    @Path("{id}")
    public Response getImageFileById(@PathParam("id") String id) {
        LOGGER.info("Received request to GET image file with ID [{}]", id);
        String json = gson.toJson(imageFileRepository.getById(id));
        return Response.ok().entity(json).build();
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
