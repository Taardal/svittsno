package no.svitts.integration.person;

public class PersonIT {

    //    @Test
//    public void createPerson() {
//        Response response = createPerson(new Person(Id.get(), "Kristian Kaland", Date.valueOf("1990-01-01"), Gender.MALE));
//        assertEquals(200, response.getStatus());
//        response.close();
//    }
//
//    @Test
//    public void updatePerson() {
//        Person person = new Person(Id.get(), "Wrong Name", Date.valueOf("1999-09-09"), Gender.FEMALE);
//        assertEquals(200, createPerson(person).getStatus());
//        assertEquals(200, updatePerson(person, "Steffen Veberg", Date.valueOf("1990-01-01"), Gender.MALE).getStatus());
//        assertPerson(getPerson(person), "Steffen Veberg", Date.valueOf("1990-01-01"), Gender.MALE);
//    }
//
//    @Test
//    public void deletePerson() {
//        Person person = new Person(Id.get(), "To Be Deleted", Date.valueOf("1990-01-01"), Gender.MALE);
//        assertEquals(200, createPerson(person).getStatus());
//        assertEquals(200, deletePerson(person).getStatus());
//    }

//    private Response createPerson(Person person) {
//        String json = getJson(person, Person.class);
//        return coreClient.request().path("person").path("new").post(json).response();
//    }
//
//    private Response updatePerson(Person person, String firstName, String lastName, KeyDate dateOfBirth, Gender gender) {
//        person.setFirstName(firstName);
//        person.setLastName(lastName);
//        person.setDateOfBirth(dateOfBirth);
//        person.setGender(gender);
//        String json = getJson(person, Person.class);
//        return coreClient.request().path("person").path("update").path(person.getId()).put(json).response();
//    }
//
//    private Person getPerson(Person person) {
//        Response response = coreClient.request().path("person").path(person.getId()).get().response();
//        String responseJson = response.readEntity(String.class);
//        return gson.fromJson(responseJson, Person.class);
//    }
//
//    private Response deletePerson(Person person) {
//        return coreClient.request().path("delete").path(person.getId()).delete().response();
//    }
//
//    private void assertPerson(Person person, String firstName, String lastName, KeyDate dateOfBirth, Gender gender) {
//        assertEquals(firstName, person.getFirstName());
//        assertEquals(lastName, person.getLastName());
//        assertEquals(dateOfBirth, person.getDateOfBirth());
//        assertEquals(gender, person.getGender());
//    }



}
