package no.svitts.core.controller;

import com.google.gson.Gson;
import no.svitts.core.movie.Movie;
import no.svitts.core.service.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/movie")
@Produces(MediaType.APPLICATION_JSON)
public class MovieController {

    private Service<Movie> movieService;

    public MovieController(Service<Movie> movieService) {
        this.movieService = movieService;
    }

    @GET
    @Path("/all")
    public String getAll() {
        List<Movie> movies = movieService.getAll();
        return new Gson().toJson(movies);
    }

    @GET
    @Path("/hello")
    public String getHello() {
        return new Gson().toJson("Hello World!");
    }

}