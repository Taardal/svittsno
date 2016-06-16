package no.svitts.core.repository;

import no.svitts.core.dao.Person;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.search.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.ResultSet;
import java.util.List;

public class PersonRepository extends CoreRepository<Person> implements Repository<Person> {

    PersonRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected List<Person> getResults(ResultSet resultSet) {
        return null;
    }

    @Override
    public Person getSingle(String id) {
        return null;
    }

    @Override
    public List<Person> getMultiple(Criteria criteria) {
        return null;
    }

    @Override
    public String insertSingle(Person person) {
        Session currentSession = getSessionFactory().getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        currentSession.save(person);
        transaction.commit();
        currentSession.close();
        return null;
    }

    @Override
    public void updateSingle(Person person) {

    }

    @Override
    public void deleteSingle(String id) {

    }
}
