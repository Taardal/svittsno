package no.svitts.core.constraint.validator;

import no.svitts.core.date.ReleaseDate;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;

import java.io.File;
import java.util.List;

public class MovieValidator {

    private static final int ID_MAX_LENGTH = 255;
    private static final int NAME_MAX_LENGTH = 255;
    private static final int IMDB_ID_MAX_LENGTH = 255;
    private static final int TAGLINE_MAX_LENGTH = 255;
    private static final int OVERVIEW_MAX_LENGTH = 510;
    private static final int VIDEO_FILE_PATH_MAX_LENGTH = 255;
    private static final int POSTER_IMAGE_FILE_PATH_MAX_LENGTH = 255;
    private static final int BACKDROP_IMAGE_FILE_PATH_MAX_LENGTH = 255;

    public boolean isIdValid(String id) {
        return id != null && !id.equals("") && id.length() <= ID_MAX_LENGTH;
    }

    public boolean isNameValid(String name) {
        return name != null && !name.equals("") && name.length() <= NAME_MAX_LENGTH;
    }

    public boolean isImdbIdValid(String imdbId) {
        return imdbId == null || imdbId.length() <= IMDB_ID_MAX_LENGTH;
    }

    public boolean isTaglineValid(String tagline) {
        return tagline == null || tagline.length() <= TAGLINE_MAX_LENGTH;
    }

    public boolean isOverviewValid(String overview) {
        return overview == null || overview.length() <= OVERVIEW_MAX_LENGTH;
    }

    public boolean isRuntimeValid(int runtime) {
        return runtime >= 0;
    }

    public boolean isReleaseDateValid(ReleaseDate releaseDate) {
        return releaseDate == null || releaseDate.getTime() > new ReleaseDate(1900, 1, 1).getTime();
    }

    public boolean isGenresValid(List<Genre> genres) {
        return genres != null;
    }

    public boolean isVideoFileValid(File videoFile) {
        return videoFile != null && videoFile.getPath().length() <= VIDEO_FILE_PATH_MAX_LENGTH;
    }

    public boolean isPosterImageFileValid(File posterImageFile) {
        return posterImageFile == null || posterImageFile.getPath().length() <= POSTER_IMAGE_FILE_PATH_MAX_LENGTH;
    }

    public boolean isBackdropImageFileValid(File backdropImageFile) {
        return backdropImageFile == null || backdropImageFile.getPath().length() <= BACKDROP_IMAGE_FILE_PATH_MAX_LENGTH;
    }

    public boolean isMovieValid(Movie movie) {
        return isIdValid(movie.getId())
                && isNameValid(movie.getName())
                && isImdbIdValid(movie.getImdbId())
                && isTaglineValid(movie.getTagline())
                && isOverviewValid(movie.getOverview())
                && isRuntimeValid(movie.getRuntime())
                && isReleaseDateValid(movie.getReleaseDate())
                && isGenresValid(movie.getGenres())
                && isVideoFileValid(movie.getVideoFile())
                && isPosterImageFileValid(movie.getPosterImageFile())
                && isBackdropImageFileValid(movie.getBackdropImageFile());
    }
}
