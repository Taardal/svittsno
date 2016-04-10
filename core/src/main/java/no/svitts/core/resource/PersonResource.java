package no.svitts.core.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.gson.deserializer.PersonDeserializer;
import no.svitts.core.person.Person;
import no.svitts.core.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/person")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource extends SvittsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonResource.class);
    private PersonService personService;
    private Gson gson;

    public PersonResource(PersonService personService) {
        this.personService = personService;
        gson = new GsonBuilder().registerTypeAdapter(Person.class, new PersonDeserializer()).create();
    }

    @GET
    @Path("/all")
    public String getAllPersons() {
        LOGGER.info("Received request to GET all persons");
        return gson.toJson(personService.getAll());
    }

    @GET
    @Path("/{id}")
    public String getPerson(@PathParam("id") String id) {
        LOGGER.info("Received request to GET person with ID {}", id);
        return gson.toJson(personService.getPerson(id));
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPerson(String json) {
        LOGGER.info("Received request to CREATE person by JSON {}", json);
        Person person = gson.fromJson(json, Person.class);
        return getResponse(personService.createPerson(person));
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson(@PathParam("id") String id, String json) {
        LOGGER.info("Received request to UPDATE person by JSON {}", json);
        Person person = gson.fromJson(json, Person.class);
        return getResponse(personService.updatePerson(person));
    }

    @DELETE
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePerson(@PathParam("id") String id) {
        LOGGER.info("Received request to DELETE person with ID {}", id);
        return getResponse(personService.deletePerson(id));
    }

}
