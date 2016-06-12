package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.ValidMovie;
import no.svitts.core.date.ReleaseDate;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovieValidator extends CoreValidator implements ConstraintValidator<ValidMovie, Movie> {

    static final int ID_MAX_LENGTH = 255;
    static final int NAME_MAX_LENGTH = 255;
    static final int IMDB_ID_MAX_LENGTH = 255;
    static final int TAGLINE_MAX_LENGTH = 255;
    static final int OVERVIEW_MAX_LENGTH = 510;
    static final int VIDEO_FILE_PATH_MAX_LENGTH = 255;
    static final int POSTER_IMAGE_FILE_PATH_MAX_LENGTH = 255;
    static final int BACKDROP_IMAGE_FILE_PATH_MAX_LENGTH = 255;

    @Override
    public void initialize(ValidMovie constraintAnnotation) {

    }

    @Override
    public boolean isValid(Movie movie, ConstraintValidatorContext constraintValidatorContext) {
        boolean idValid = isValid(getIdViolations(movie.getId()), constraintValidatorContext);
        boolean nameValid = isValid(getNameViolations(movie.getName()), constraintValidatorContext);
        boolean imdbIdValid = isValid(getImdbIdViolations(movie.getImdbId()), constraintValidatorContext);
        boolean taglineValid = isValid(getTaglineViolations(movie.getTagline()), constraintValidatorContext);
        boolean overviewValid = isValid(getOverviewViolations(movie.getOverview()), constraintValidatorContext);
        boolean runtimeValid = isValid(getRuntimeViolations(movie.getRuntime()), constraintValidatorContext);
        boolean releaseDateValid = isValid(getReleaseDateViolations(movie.getReleaseDate()), constraintValidatorContext);
        boolean genresValid = isValid(getGenresViolations(movie.getGenres()), constraintValidatorContext);
        boolean videoFileValid = isValid(getVideoFileViolations(movie.getVideoFile()), constraintValidatorContext);
        boolean posterImageFileValid = isValid(getPosterImageFileViolations(movie.getPosterImageFile()), constraintValidatorContext);
        boolean backdropImageFileValid = isValid(getBackdropImageFileViolations(movie.getBackdropImageFile()), constraintValidatorContext);
        return idValid
                && nameValid
                && imdbIdValid
                && taglineValid
                && overviewValid
                && runtimeValid
                && releaseDateValid
                && genresValid
                && videoFileValid
                && posterImageFileValid
                && backdropImageFileValid;
    }

    private boolean isValid(List<String> violations, ConstraintValidatorContext constraintValidatorContext) {
        if (violations.size() == 0) {
            return true;
        } else {
            for (String violation : violations) {
                addConstraintViolation(violation, constraintValidatorContext);
            }
            return false;
        }
    }

    private List<String> getIdViolations(String id) {
        List<String> violations = new ArrayList<>();
        if (id != null && !id.equals("")) {
            if (id.length() > ID_MAX_LENGTH) {
                violations.add("ID was too long.");
            }
            if (containsIllegalCharacters(id)) {
                violations.add("ID contained illegal characters.");
            }
        } else {
            violations.add("ID was missing.");
        }
        return violations;
    }

    private List<String> getNameViolations(String name) {
        List<String> violations = new ArrayList<>();
        if (name != null && !name.equals("")) {
            if (name.length() > NAME_MAX_LENGTH) {
                violations.add("Name was too long.");
            }
            if (containsIllegalCharacters(name)) {
                violations.add("Name contained illegal characters.");
            }
        } else {
            violations.add("Name was missing.");
        }
        return violations;
    }

    private List<String> getImdbIdViolations(String imdbId) {
        List<String> violations = new ArrayList<>();
        if (imdbId != null && !imdbId.equals("")) {
            if (imdbId.length() > IMDB_ID_MAX_LENGTH) {
                violations.add("IMDB ID was too long.");
            }
            if (containsIllegalCharacters(imdbId)) {
                violations.add("IMDB ID contained illegal characters.");
            }
        }
        return violations;
    }

    private List<String> getTaglineViolations(String tagline) {
        List<String> violations = new ArrayList<>();
        if (tagline != null && !tagline.equals("")) {
            if (tagline.length() > TAGLINE_MAX_LENGTH) {
                violations.add("Tagline was too long.");
            }
            if (containsIllegalCharacters(tagline)) {
                violations.add("Tagline contained illegal characters.");
            }
        }
        return violations;
    }

    private List<String> getOverviewViolations(String overview) {
        List<String> violations = new ArrayList<>();
        if (overview != null && !overview.equals("")) {
            if (overview.length() > OVERVIEW_MAX_LENGTH) {
                violations.add("Overview was too long.");
            }
            if (containsIllegalCharacters(overview)) {
                violations.add("Overview contained illegal characters.");
            }
        }
        return violations;
    }

    private List<String> getRuntimeViolations(int runtime) {
        List<String> violations = new ArrayList<>();
        if (runtime < 0) {
            violations.add("Runtime was smaller than 0 (zero).");
        }
        return violations;
    }

    private List<String> getReleaseDateViolations(ReleaseDate releaseDate) {
        List<String> violations = new ArrayList<>();
        if (releaseDate != null) {
            if (releaseDate.getTime() < new ReleaseDate(1900, 1, 1).getTime()) {
                violations.add("Release date was before 01.01.1900");
            }
        }
        return violations;
    }

    private List<String> getGenresViolations(List<Genre> genres) {
        List<String> violations = new ArrayList<>();
        if (genres != null) {
            violations.addAll(genres.stream().filter(genre -> genre == null).map(genre -> "A genre was invalid or not supported.").collect(Collectors.toList()));
        }
        return violations;
    }

    private List<String> getVideoFileViolations(File videoFile) {
        List<String> violations = new ArrayList<>();
        if (videoFile != null) {
            if (videoFile.getPath().length() > VIDEO_FILE_PATH_MAX_LENGTH) {
                violations.add("Video file path was too long.");
            }
            if (containsIllegalCharacters(videoFile.getPath())) {
                violations.add("Video file path contained illegal characters.");
            }
        }
        return violations;
    }

    private List<String> getPosterImageFileViolations(File posterImageFile) {
        List<String> violations = new ArrayList<>();
        if (posterImageFile != null) {
            if (posterImageFile.getPath().length() > POSTER_IMAGE_FILE_PATH_MAX_LENGTH) {
                violations.add("Poster image file path was too long.");
            }
            if (containsIllegalCharacters(posterImageFile.getPath())) {
                violations.add("Poster image file path contained illegal characters.");
            }
        }
        return violations;
    }

    private List<String> getBackdropImageFileViolations(File backdropImageFile) {
        List<String> violations = new ArrayList<>();
        if (backdropImageFile != null) {
            if (backdropImageFile.getPath().length() > BACKDROP_IMAGE_FILE_PATH_MAX_LENGTH) {
                violations.add("Backrop image file path was too long.");
            }
            if (containsIllegalCharacters(backdropImageFile.getPath())) {
                violations.add("Backrop image file path contained illegal characters.");
            }
        }
        return violations;
    }

}
