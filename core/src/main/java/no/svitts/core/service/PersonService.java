package no.svitts.core.service;

import no.svitts.core.date.KeyDate;
import no.svitts.core.person.Gender;
import no.svitts.core.person.Job;
import no.svitts.core.person.Person;
import no.svitts.core.person.UnknownPerson;
import no.svitts.core.repository.Repository;

import java.util.List;
import java.util.Map;

public class PersonService {

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

    public Person getPerson(String firstName, String lastName, KeyDate dateOfBirth, Gender gender) {
        return personRepository.getByAttributes(firstName, lastName, dateOfBirth, gender);
    }

    public boolean createPersons(Map<Job, List<Person>> persons) {
        boolean personsCreated = true;
        for (List<Person> personList : persons.values()) {
            for (Person person : personList) {
                if (!alreadyExists(person)) {
                    boolean personCreated = createPerson(person);
                    if (!personCreated) {
                        personsCreated = false;
                    }
                }
            }
        }
        return personsCreated;
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

    private boolean alreadyExists(Person person) {
        Person queriedPerson = getPerson(person.getFirstName(), person.getLastName(), person.getDateOfBirth(), person.getGender());
        return !(queriedPerson instanceof UnknownPerson)
                && person.getFirstName().equals(queriedPerson.getFirstName())
                && person.getLastName().equals(queriedPerson.getLastName())
                && person.getDateOfBirth().getTime() == queriedPerson.getDateOfBirth().getTime()
                && person.getGender() == queriedPerson.getGender();
    }
}
