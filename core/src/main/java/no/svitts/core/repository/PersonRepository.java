package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.date.KeyDate;
import no.svitts.core.person.Gender;
import no.svitts.core.person.Person;
import no.svitts.core.person.UnknownPerson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository extends MySqlRepository<Person> implements Repository<Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepository.class);

    public PersonRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Person> getAll() {
        LOGGER.info("Getting all persons");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement selectAllPersonsPreparedStatement = getSelectAllPersonsPreparedStatement(connection)) {
                return executeQuery(selectAllPersonsPreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get all persons", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Person getById(String id) {
        LOGGER.info("Getting person with ID {}", id);
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement selectPersonPreparedStatement = getSelectPersonPreparedStatement(id, connection)) {
                return executeQuery(selectPersonPreparedStatement).get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get person with ID {}", id, e);
            return new UnknownPerson();
        }
    }

    @Override
    public boolean insertSingle(Person person) {
        LOGGER.info("Creating person {}", person);
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement insertPersonPreparedStatement = getInsertPersonPreparedStatement(person, connection)) {
                return executeUpdate(insertPersonPreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not create person {}", person, e);
            return false;
        }
    }

    @Override
    public boolean updateSingle(Person person) {
        LOGGER.info("Updating person with values [" + person.toString() + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement updatePersonPreparedStatement = getUpdatePersonPreparedStatement(person, connection)) {
                return executeUpdate(updatePersonPreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not update person {}", person, e);
            return false;
        }
    }

    @Override
    public boolean deleteSingle(String id) {
        LOGGER.info("Deleting person with ID {}", id);
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement deletePersonPreparedStatement = getDeletePersonPreparedStatement(id, connection)) {
                return executeUpdate(deletePersonPreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not delete person with ID {}", id, e);
            return false;
        }
    }

    @Override
    protected List<Person> getResults(ResultSet resultSet) throws SQLException {
        List<Person> persons = new ArrayList<>();
        while (resultSet.next()) {
            Person person = new Person(resultSet.getString("id"));
            person.setFirstName(resultSet.getString("first_name"));
            person.setLastName(resultSet.getString("last_name"));
            person.setDateOfBirth(new KeyDate(resultSet.getDate("date_of_birth")));
            person.setGender(Gender.valueOf(resultSet.getString("gender")));
            persons.add(person);
        }
        LOGGER.info("Got person(s) {}", persons.toString());
        return persons;
    }

    private PreparedStatement getSelectAllPersonsPreparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM person;");
    }

    private PreparedStatement getSelectPersonPreparedStatement(String id, Connection connection) throws SQLException {
        String query = "SELECT * FROM person WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

    private PreparedStatement getInsertPersonPreparedStatement(Person person, Connection connection) throws SQLException {
        String statement = "INSERT INTO person VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, person.getId());
        preparedStatement.setString(2, person.getFirstName());
        preparedStatement.setString(3, person.getLastName());
        preparedStatement.setDate(4, person.getDateOfBirth().toJavaSqlDate());
        preparedStatement.setString(5, person.getGender().toString());
        return preparedStatement;
    }

    private PreparedStatement getUpdatePersonPreparedStatement(Person person, Connection connection) throws SQLException {
        String statement = "UPDATE person SET first_name = ?, last_name = ?, age = ?, gender = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, person.getFirstName());
        preparedStatement.setString(2, person.getLastName());
        preparedStatement.setDate(3, person.getDateOfBirth().toJavaSqlDate());
        preparedStatement.setString(4, person.getGender().toString());
        preparedStatement.setString(5, person.getId());
        return preparedStatement;
    }

    private PreparedStatement getDeletePersonPreparedStatement(String id, Connection connection) throws SQLException {
        String statement = "DELETE FROM person WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

}
