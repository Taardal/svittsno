package no.svitts.core.service;

import no.svitts.core.person.Person;
import no.svitts.core.repository.Repository;

import java.util.List;
import java.util.logging.Logger;

public class PersonService {

    private static final Logger LOGGER = Logger.getLogger(PersonService.class.getName());
    private Repository<Person> personRepository;

    public PersonService(Repository<Person> personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAll() {
        return personRepository.getAll();
    }

    public Person getPerson(String id) {
        return personRepository.getById(id);
    }

    public boolean createPerson(Person person) {
        return personRepository.insertSingle(person);
    }

    public boolean updatePerson(Person person) {
        return personRepository.updateSingle(person);
    }

    public boolean deletePerson(String id) {
        return personRepository.deleteSingle(id);
    }

    public boolean createPersons(List<Person> persons) {
        for (Person person : persons) {
            if (!alreadyExists(person)) {

            }
        }
        return false;
    }

    void alreadyExists(Person person) {

    }
}
