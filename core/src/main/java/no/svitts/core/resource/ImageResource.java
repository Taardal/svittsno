package no.svitts.core.resource;

import com.google.gson.Gson;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/image")
@Produces(MediaType.APPLICATION_JSON)
public class ImageResource {

    @GET
    @Path("/hello")
    public String getHello() {
        return new Gson().toJson("Hello World!");
    }

}
