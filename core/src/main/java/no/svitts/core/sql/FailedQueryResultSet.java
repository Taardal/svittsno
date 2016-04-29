package no.svitts.core.sql;

import java.sql.SQLException;

public class FailedQueryResultSet extends DefaultResultSet {

    @Override
    public boolean next() throws SQLException {
        return false;
    }

    @Override
    public void close() throws SQLException {
        //Closed...
    }
}
