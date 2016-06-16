package no.svitts.core.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDao implements Dao<Person> {

    private Connection  connection;

    public PersonDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Person> getAll() {
        String sql = "SELECT * FROM person;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Person> persons = new ArrayList<>();
            while (resultSet.next()) {
                persons.add(new Person());
            }
            return persons;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person getSingle(String id) {
        String sql = "SELECT * FROM person p WHERE p.id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Person> persons = new ArrayList<>();
            while (resultSet.next()) {
                persons.add(new Person());
            }
            return persons.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update() {
        return false;
    }
}
