package no.svitts.core;

import no.svitts.core.database.LocalDatabase;
import no.svitts.core.datasource.DataSource;
import no.svitts.core.datasource.DataSourceConfig;
import no.svitts.core.datasource.MySqlDataSource;
import no.svitts.core.file.VideoFile;
import no.svitts.core.movie.Movie;
import no.svitts.core.person.Person;
import no.svitts.core.repository.MovieRepository;
import no.svitts.core.repository.PersonRepository;
import no.svitts.core.repository.Repository;
import no.svitts.core.repository.VideoFileRepository;
import no.svitts.core.resource.MovieResource;
import no.svitts.core.resource.PersonResource;
import no.svitts.core.service.MovieService;
import no.svitts.core.service.PersonService;
import org.constretto.ConstrettoBuilder;
import org.constretto.ConstrettoConfiguration;
import org.constretto.model.ClassPathResource;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

public class CoreApplication extends ResourceConfig {

    private static final Logger LOGGER = Logger.getLogger(CoreApplication.class.getName());

    public CoreApplication() {
        ConstrettoConfiguration constrettoConfiguration = getConstrettoConfiguration();
        DataSourceConfig dataSourceConfig = constrettoConfiguration.as(DataSourceConfig.class);
        DataSource dataSource = new MySqlDataSource(dataSourceConfig);
        if (constrettoConfiguration.getCurrentTags().get(0).equals("LOCAL")) {
            new LocalDatabase(dataSource);
        }
        register(getPersonResource(dataSource));
    }

    private MovieResource getMovieResource(DataSource dataSource) {
        Repository<VideoFile> videoFileRepository = new VideoFileRepository(dataSource);
        Repository<Movie> movieRepository = new MovieRepository(dataSource);
        Repository<Person> personRepository = new PersonRepository(dataSource);
        MovieService movieService = new MovieService(movieRepository, videoFileRepository, personRepository);
        return new MovieResource(movieService);
    }

    private PersonResource getPersonResource(DataSource dataSource) {
        Repository<Person> personRepository = new PersonRepository(dataSource);
        PersonService personService = new PersonService(personRepository);
        return new PersonResource(personService);
    }

    private ConstrettoConfiguration getConstrettoConfiguration() {
        return new ConstrettoBuilder()
                .createPropertiesStore()
                .addResource(new ClassPathResource("application.properties"))
                .done()
                .getConfiguration();
    }

}