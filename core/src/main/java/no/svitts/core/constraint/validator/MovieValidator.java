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
import java.util.Set;
import java.util.stream.Collectors;

import static no.svitts.core.movie.Movie.*;

public class MovieValidator extends CoreValidator implements ConstraintValidator<ValidMovie, Movie> {

    @Override
    public void initialize(ValidMovie constraintAnnotation) {

    }

    @Override
    public boolean isValid(Movie movie, ConstraintValidatorContext constraintValidatorContext) {
        return isNoViolations(getIdViolations(movie.getId()), constraintValidatorContext)
                && isNoViolations(getNameViolations(movie.getName()), constraintValidatorContext)
                && isNoViolations(getImdbIdViolations(movie.getImdbId()), constraintValidatorContext)
                && isNoViolations(getTaglineViolations(movie.getTagline()), constraintValidatorContext)
                && isNoViolations(getOverviewViolations(movie.getOverview()), constraintValidatorContext)
                && isNoViolations(getRuntimeViolations(movie.getRuntime()), constraintValidatorContext)
                && isNoViolations(getReleaseDateViolations(movie.getReleaseDate()), constraintValidatorContext)
                && isNoViolations(getGenresViolations(movie.getGenres()), constraintValidatorContext)
                && isNoViolations(getVideoFileViolations(movie.getVideoFile()), constraintValidatorContext)
                && isNoViolations(getPosterImageFileViolations(movie.getPosterImageFile()), constraintValidatorContext)
                && isNoViolations(getBackdropImageFileViolations(movie.getBackdropImageFile()), constraintValidatorContext);
    }

    private List<String> getIdViolations(String id) {
        List<String> violations = new ArrayList<>();
        if (id != null && !id.equals("")) {
            if (id.length() > ID_MAX_LENGTH) {
                violations.add("ID was too long.");
            }
            if (isContainsIllegalCharacters(id)) {
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
            if (isContainsIllegalCharacters(name)) {
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
            if (isContainsIllegalCharacters(imdbId)) {
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
            if (isContainsIllegalCharacters(tagline)) {
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
            if (isContainsIllegalCharacters(overview)) {
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

    private List<String> getGenresViolations(Set<Genre> genres) {
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
            if (isContainsIllegalCharacters(videoFile.getPath())) {
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
            if (isContainsIllegalCharacters(posterImageFile.getPath())) {
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
            if (isContainsIllegalCharacters(backdropImageFile.getPath())) {
                violations.add("Backrop image file path contained illegal characters.");
            }
        }
        return violations;
    }

}
