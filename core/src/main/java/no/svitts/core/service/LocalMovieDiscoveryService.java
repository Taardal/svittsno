package no.svitts.core.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.file.VideoFile;
import no.svitts.core.json.deserializer.MovieDeserializer;
import no.svitts.core.movie.Movie;
import no.svitts.core.search.Search;
import no.svitts.core.search.SearchKey;
import no.svitts.core.util.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalMovieDiscoveryService implements LocalDiscoveryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMovieDiscoveryService.class);
    private static final int MAX_DEPTH = 3;

    private Service<Movie> movieService;
    private PathMatcher videoFileMatcher;
    private Gson gson;

    @Inject
    public LocalMovieDiscoveryService(Service<Movie> movieService) {
        this.movieService = movieService;
        videoFileMatcher = getVideoFileMatcher();
        gson = getGson();
    }

    @Override
    public List<Movie> discover(String path) {
        return getVideoFilePaths(path)
                .stream()
                .map(this::getMovie)
                .filter(movie -> {
                    List<Movie> existingMoviesWithTitle = movieService.search(new Search(movie.getTitle(), SearchKey.TITLE));
                    LOGGER.debug("Existing movies with title [{}]", existingMoviesWithTitle);
                    List<Movie> existingMoviesWithVideoFilePath = movieService.search(new Search(movie.getVideoFile().getPath(), SearchKey.VIDEO_FILE_PATH));
                    LOGGER.debug("Existing movies with video file path [{}]", existingMoviesWithVideoFilePath);
                    return existingMoviesWithTitle.isEmpty() && existingMoviesWithVideoFilePath.isEmpty();
                })
                .collect(Collectors.toList());
    }

    private PathMatcher getVideoFileMatcher() {
        String videoFormatsCommaSeparated = Arrays.stream(VideoFile.VIDEO_FORMATS).collect(Collectors.joining(","));
        return FileSystems.getDefault().getPathMatcher("glob:*.{" + videoFormatsCommaSeparated + "}");
    }

    private Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(Movie.class, new MovieDeserializer()).create();
    }

    private Set<Path> getVideoFilePaths(String target) {
        return walkFiles(target, MAX_DEPTH).filter(path -> Files.isRegularFile(path) && videoFileMatcher.matches(path.getFileName())).collect(Collectors.toSet());
    }

    private Stream<Path> walkFiles(String path, int maxDepth) {
        return walkFiles(Paths.get(path), maxDepth);
    }

    private Stream<Path> walkFiles(Path path, int maxDepth) {
        try {
            return Files.walk(path, maxDepth);
        } catch (IOException e) {
            LOGGER.error("Could not walk files from path [{}] with max depth [{}]", path, maxDepth, e);
            throw new RuntimeException(e);
        }
    }

    private Movie getMovie(Path videoFilePath) {
        String fileNameWithoutExtension = getFileNameWithoutExtension(videoFilePath);
        Movie movie = getMovieFromJsonInfoFile(videoFilePath, fileNameWithoutExtension);
        if (movie.getId().isEmpty()) {
            movie.setId(Id.get());
        }
        if (movie.getTitle().isEmpty()) {
            movie.setTitle(fileNameWithoutExtension);
        }
        if (movie.getVideoFile().getPath().isEmpty()) {
            String videoFormat = movie.getVideoFile().getVideoFormat();
            String audioFormat = movie.getVideoFile().getAudioFormat();
            movie.setVideoFile(new VideoFile(videoFilePath.toString(), videoFormat, audioFormat));
        }
        return movie;
    }

    private String getFileNameWithoutExtension(Path path) {
        String fileName = path.getFileName().toString();
        int i = fileName.lastIndexOf(".");
        if (i >= 0) {
            fileName = fileName.substring(0, i);
        }
        return fileName;
    }

    private Movie getMovieFromJsonInfoFile(Path videoFilePath, String fileName) {
        MovieBuilder movieBuilder = new MovieBuilder();
        DirectoryStream<Path> jsonFilePaths = getSiblingFilePaths(videoFilePath, fileName + "{.json}");
        jsonFilePaths.forEach(jsonFilePath -> {
            String json = new String(getBytes(jsonFilePath));
            Movie movie = gson.fromJson(json, Movie.class);
            movieBuilder.from(movie);
        });
        return movieBuilder.build();
    }

    private DirectoryStream<Path> getSiblingFilePaths(Path path, String glob) {
        return getDirectoryStream(path.getParent(), glob);
    }

    private DirectoryStream<Path> getDirectoryStream(Path path, String glob) {
        try {
            return Files.newDirectoryStream(path, glob);
        } catch (IOException e) {
            LOGGER.error("Could not get new directory stream from path [{}] with glob [{}]", path, glob, e);
            throw new RuntimeException(e);
        }
    }

    private byte[] getBytes(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            LOGGER.error("Could not read bytes from file with path [{}]", path, e);
            throw new RuntimeException(e);
        }
    }

}
