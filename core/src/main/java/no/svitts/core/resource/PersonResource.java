package no.svitts.core.resource;

import com.google.gson.Gson;
import no.svitts.core.person.Person;
import no.svitts.core.repository.Repository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/person")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    private Repository<Person> personRepository;
    private Gson gson;

    public PersonResource(Repository<Person> personRepository) {
        this.personRepository = personRepository;
        gson = new Gson();
    }

    @GET
    @Path("/all")
    public String getAll() {
        return gson.toJson(personRepository.getAll());
    }

}
