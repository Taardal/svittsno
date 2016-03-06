package no.svitts.core.repository;

import no.svitts.core.database.DataSource;
import no.svitts.core.database.SvittsDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractRepository<T> {

    private static final Logger LOGGER = Logger.getLogger(SvittsDataSource.class.getName());
    protected DataSource dataSource;

    protected AbstractRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected int executeUpdate(PreparedStatement preparedStatement) throws SQLException {
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            preparedStatement.getConnection().close();
        }
        return 0;
    }

    protected List<T> executeQuery(PreparedStatement preparedStatement) throws SQLException {
        try {
            return getResults(preparedStatement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            preparedStatement.getConnection().close();
        }
        return new ArrayList<>();
    }

    protected List<T> getResults(ResultSet resultSet) throws SQLException {
        try {
            return getResultsFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultSet.close();;
        }
        return new ArrayList<>();
    }

    protected List<T> getResultsFromResultSet(ResultSet resultSet) throws SQLException {
        List<T> results = new ArrayList<>();
        while (resultSet.next()) {
            //results.add(result)
        }
        return results;
    }

}
