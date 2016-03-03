package no.svitts.core.resource;

import com.google.gson.Gson;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/movie")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {

    private Service<Movie> movieService;

    public MovieResource(Service<Movie> movieService) {
        this.movieService = movieService;
    }

    @GET
    @Path("/all")
    public String getAll() {
        return new Gson().toJson(movieService.getAll());
    }

    @GET
    @Path("/single")
    public String getSingle() {
        return new Gson().toJson(movieService.getById(1));
    }


    @GET
    @Path("/hello")
    public String getHello() {
        return new Gson().toJson("Hello World!");
    }

}