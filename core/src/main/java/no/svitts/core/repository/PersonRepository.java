package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;
import no.svitts.core.person.Gender;
import no.svitts.core.person.Person;
import no.svitts.core.person.UnknownPerson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonRepository extends MySqlRepository<Person> implements Repository<Person> {

    private static final Logger LOGGER = Logger.getLogger(PersonRepository.class.getName());

    public PersonRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Person> getAll() {
        LOGGER.log(Level.INFO, "Getting all persons");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement selectAllPersonsPreparedStatement = getSelectAllPersonsPreparedStatement(connection)) {
                return executeQuery(selectAllPersonsPreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not get all persons", e);
        }
        return new ArrayList<>();
    }

    @Override
    public Person getById(String id) {
        LOGGER.log(Level.SEVERE, "Getting person with ID [" + id + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement selectPersonPreparedStatement = getSelectPersonPreparedStatement(id, connection)) {
                return executeQuery(selectPersonPreparedStatement).get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not get person with ID [" + id + "]", e);
        }
        return new UnknownPerson();
    }

    @Override
    public boolean insertSingle(Person person) {
        LOGGER.log(Level.SEVERE, "Creating person [" + person.toString() + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement insertPersonPreparedStatement = getInsertPersonPreparedStatement(person, connection)) {
                return executeUpdate(insertPersonPreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not create person [" + person.toString() + "]", e);
        }
        return false;
    }

    @Override
    public boolean updateSingle(Person person) {
        LOGGER.log(Level.SEVERE, "Updating person with values [" + person.toString() + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement updatePersonPreparedStatement = getUpdatePersonPreparedStatement(person, connection)) {
                return executeUpdate(updatePersonPreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not update person [" + person.toString() + "]", e);
        }
        return false;
    }

    @Override
    public boolean deleteSingle(String id) {
        LOGGER.log(Level.SEVERE, "Deleting person with ID [" + id + "]");
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement deletePersonPreparedStatement = getDeletePersonPreparedStatement(id, connection)) {
                return executeUpdate(deletePersonPreparedStatement);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Could not delete person with ID [" + id + "]", e);
        }
        return false;
    }

    @Override
    protected List<Person> getResults(ResultSet resultSet) throws SQLException {
        List<Person> persons = new ArrayList<>();
        while (resultSet.next()) {
            Person person = new Person(resultSet.getString("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setGender(Gender.valueOf(resultSet.getString("gender")));
            persons.add(person);
        }
        LOGGER.log(Level.INFO, "Got person(s) " + persons.toString() + "");
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
        String statement = "INSERT INTO person VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, person.getId());
        preparedStatement.setString(2, person.getName());
        preparedStatement.setInt(3, person.getAge());
        preparedStatement.setString(4, person.getGender().toString());
        return preparedStatement;
    }

    private PreparedStatement getUpdatePersonPreparedStatement(Person person, Connection connection) throws SQLException {
        String statement = "UPDATE person SET name = ?, age = ?, gender = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, person.getName());
        preparedStatement.setInt(2, person.getAge());
        preparedStatement.setString(3, person.getGender().toString());
        preparedStatement.setString(4, person.getId());
        return preparedStatement;
    }

    private PreparedStatement getDeletePersonPreparedStatement(String id, Connection connection) throws SQLException {
        String statement = "DELETE FROM person WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        preparedStatement.setString(1, id);
        return preparedStatement;
    }

}
