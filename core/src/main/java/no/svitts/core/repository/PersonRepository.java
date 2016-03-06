package no.svitts.core.repository;

import no.svitts.core.database.DataSource;
import no.svitts.core.person.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository extends AbstractRepository<Person> implements Repository<Person> {

    protected PersonRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Person> getAll() {
        try {
            return executeQuery(getAllPersonsPreparedStatement(dataSource.getConnection()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Person getById(int id) {
        try {
            PreparedStatement personByIdPreparedStatement = getPersonByIdPreparedStatement(id, dataSource.getConnection());
            return executeQuery(personByIdPreparedStatement).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Person();
    }

    @Override
    public int update(int id) {
        try {
            return executeUpdate(getUpdatePersonByIdPreparedStatement(id, dataSource.getConnection()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(int id) {
        try {
            return executeUpdate(getDeletePersonByIdPreparedStatement(id, dataSource.getConnection()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private PreparedStatement getUpdatePersonByIdPreparedStatement(int id, Connection connection) {
        return null;
    }

    private PreparedStatement getDeletePersonByIdPreparedStatement(int id, Connection connection) {
        return null;
    }

    private PreparedStatement getAllPersonsPreparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM person;");
    }

    private PreparedStatement getPersonByIdPreparedStatement(int id, Connection connection) throws SQLException {
        String query = "SELECT * FROM person WHERE person.id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }

    public List<Person> getAllPersons(Connection connection) throws SQLException {
        try {
            return executeQuery(getAllPersonsPreparedStatement(connection));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return new ArrayList<>();
    }

    private Person getPerson(int id, Connection connection) throws SQLException {
        try {
            return executeQuery(getPersonByIdPreparedStatement(id, connection)).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return new Person();
    }
}
