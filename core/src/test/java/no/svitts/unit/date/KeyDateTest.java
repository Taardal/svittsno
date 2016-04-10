package no.svitts.unit.date;

import no.svitts.core.date.KeyDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KeyDateTest {

    @Test
    public void toJavaSqlDateShouldReturnJavaSqlDateWithSameTime() {
        KeyDate keyDate = new KeyDate(2016, 1, 1);
        java.sql.Date date = keyDate.toJavaSqlDate();
        assertTrue(date != null);
        assertEquals(date.getTime(), keyDate.getTime());
    }

    @Test
    public void toJavUtilDateShouldReturnJavaUtilDateWithSameTime() {
        KeyDate keyDate = new KeyDate(2016, 1, 1);
        java.util.Date date = keyDate.toJavaUtilDate();
        assertTrue(date != null);
        assertEquals(date.getTime(), keyDate.getTime());
    }

//    @Test
//    public void foo() {
//        Map<Job, List<Person>> persons = new HashMap<>();
//        persons.put(Job.DIRECTOR, new ArrayList<>());
//        persons.get(Job.DIRECTOR).add(new Person(Id.get(), "Will", "Smith", new KeyDate(1970, 1, 1), Gender.MALE));
//        persons.get(Job.DIRECTOR).add(new Person(Id.get(), "Jada", "Pinkett Smith", new KeyDate(1970, 1, 1), Gender.FEMALE));
//        persons.put(Job.ACTOR, new ArrayList<>());
//        persons.get(Job.ACTOR).add(new Person(Id.get(), "Jaden", "Smith", new KeyDate(1990, 1, 1), Gender.MALE));
//        persons.get(Job.ACTOR).add(new Person(Id.get(), "Willow", "Smith", new KeyDate(1990, 1, 1), Gender.FEMALE));
//        boolean success = true;
//        for (List<Person> personList : persons.values()) {
//            for (Person person : personList) {
//                success = false;
//            }
//        }
//        assertFalse(success);
//    }

}
