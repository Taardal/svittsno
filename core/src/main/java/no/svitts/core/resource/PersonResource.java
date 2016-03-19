package no.svitts.core.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.gson.deserializer.PersonDeserializer;
import no.svitts.core.person.Person;
import no.svitts.core.service.PersonService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/person")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource extends SvittsResource {

    private static final Logger LOGGER = Logger.getLogger(PersonResource.class.getName());
    private PersonService personService;
    private Gson gson;

    public PersonResource(PersonService personService) {
        this.personService = personService;
        gson = new GsonBuilder().registerTypeAdapter(Person.class, new PersonDeserializer()).create();
    }

    @GET
    @Path("/{id}")
    public String getPerson(@PathParam("id") String id) {
        return gson.toJson(personService.getPerson(id));
    }

    @GET
    @Path("/all")
    public String getAllPersons() {
        return gson.toJson(personService.getAllPersons());
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPerson(String json) {
        LOGGER.log(Level.INFO, "Received JSON [" + json + "]");
        Person person = gson.fromJson(json, Person.class);
        return getRespone(personService.createPerson(person));
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson(@PathParam("id") String id, String json) {
        LOGGER.log(Level.INFO, "Received JSON [" + json + "]");
        Person person = gson.fromJson(json, Person.class);
        return getRespone(personService.updatePerson(person));
    }

    @PUT
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePerson(@PathParam("id") String id) {
        return getRespone(personService.deletePerson(id));
    }

}
