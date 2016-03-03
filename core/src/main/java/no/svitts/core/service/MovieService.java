package no.svitts.core.service;

import no.svitts.core.file.VideoFile;
import no.svitts.core.movie.Movie;
import no.svitts.core.repository.Repository;

import java.util.List;

public class MovieService implements Service<Movie> {

    private Repository<Movie> movieRepository;
    private Repository<VideoFile> videoFileRepository;

    public MovieService(Repository<Movie> movieRepository, Repository<VideoFile> videoFileRepository) {
        this.movieRepository = movieRepository;
        this.videoFileRepository = videoFileRepository;
    }

    public List<Movie> getAll() {
        return enrichMovies(movieRepository.getAll());
    }

    @Override
    public Movie getById(int id) {
        return enrichMovie(movieRepository.getById(id));
    }

    private List<Movie> enrichMovies(List<Movie> movies) {
        movies.forEach(this::enrichMovie);
        return movies;
    }

    private Movie enrichMovie(Movie movie) {
        movie.setVideoFile(videoFileRepository.getById(movie.getId()));
        return movie;
    }

}
