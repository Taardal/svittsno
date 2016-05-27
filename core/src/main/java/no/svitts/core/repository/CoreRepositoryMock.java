package no.svitts.core.repository;

import no.svitts.core.datasource.DataSource;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

class CoreRepositoryMock extends CoreRepository<String> {

    CoreRepositoryMock(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected List<String> getResults(ResultSet resultSet) {
        List<String> results = new ArrayList<>();
        results.add("result");
        return results;
    }
}
