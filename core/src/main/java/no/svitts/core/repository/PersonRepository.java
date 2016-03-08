package no.svitts.core.repository;

import no.svitts.core.database.DataSource;
import no.svitts.core.person.Gender;
import no.svitts.core.person.Person;
import no.svitts.core.person.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonRepository extends SqlRepository<Person> implements Repository<Person> {

    private static final Logger LOGGER = Logger.getLogger(PersonRepository.class.getName());

    protected PersonRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Person> getAll() {
        try {
            PreparedStatement allPersonsPreparedStatement = getAllPersonsPreparedStatement(dataSource.getConnection());
            return executeQuery(allPersonsPreparedStatement);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not get all persons");
        }
        return new ArrayList<>();
    }

    @Override
    public Person getById(int id) {
        try {
            PreparedStatement personPreparedStatement = getPersonPreparedStatement(id, dataSource.getConnection());
            return executeQuery(personPreparedStatement).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not get person with ID: [" + id + "]");
        }
        return new Person(0);
    }

    @Override
    public int updateSingle(int id) {
        try {
            PreparedStatement updatePersonPreparedStatement = getUpdatePersonPreparedStatement(id, dataSource.getConnection());
            return executeUpdate(updatePersonPreparedStatement);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not update person with ID: [" + id + "]");
        }
        return UPDATE_ERROR_CODE;
    }

    @Override
    public int deleteSingle(int id) {
        try {
            PreparedStatement deletePersonPreparedStatement = getDeletePersonPreparedStatement(id, dataSource.getConnection());
            return executeUpdate(deletePersonPreparedStatement);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not delete person with ID: [" + id + "]");
        }
        return UPDATE_ERROR_CODE;
    }

    @Override
    protected List<Person> getResults(ResultSet resultSet) throws SQLException {
        List<Person> persons = new ArrayList<>();
        while (resultSet.next()) {
            Person person = new Person(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setGender(Gender.valueOf(resultSet.getString("gender")));
            person.setRole(Role.valueOf(resultSet.getString("role")));
            persons.add(person);
        }
        LOGGER.log(Level.INFO, "Got person(s) [" + persons.toString() + "]");
        return persons;
    }

    private PreparedStatement getAllPersonsPreparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM person;");
    }

    private PreparedStatement getPersonPreparedStatement(int id, Connection connection) throws SQLException {
        String query = "SELECT * FROM person WHERE person.id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }

    private PreparedStatement getUpdatePersonPreparedStatement(int id, Connection connection) throws SQLException {
        String query = "";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }

    private PreparedStatement getDeletePersonPreparedStatement(int id, Connection connection) throws SQLException {
        String query = "";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }

}
