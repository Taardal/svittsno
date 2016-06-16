package no.svitts.core.dao;

import no.svitts.core.datasource.CoreDataSource;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.DataSourceConfig;

import java.util.List;

public class Foo {

    void foo() {
        DataSource dataSource = new CoreDataSource(new DataSourceConfig("", "", "", ""));
        DaoManager<Person> daoManager = new DaoManager<>(new PersonDao(null), dataSource);

        List<Person> persons = daoManager.executeTransaction(Dao::getAll);
        Person person = daoManager.executeTransaction(dao -> dao.getSingle("1"));
        List<Person> persons1 = daoManager.executeTransaction(new Transaction<Person, List<Person>>() {
            @Override
            public List<Person> execute(Dao<Person> dao) {
                return dao.getAll();
            }
        });

    }

}
