package no.svitts.core.constraint.validator;

import no.svitts.core.builder.MovieBuilder;
import no.svitts.core.genre.Genre;
import no.svitts.core.movie.Movie;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static no.svitts.core.constraint.validator.MovieValidator.*;
import static no.svitts.core.util.StringUtil.getRandomString;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MovieValidatorTest {

    private MovieValidator movieValidator;
    private MovieBuilder movieBuilder;
    private String[] illegalCharacters;
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Before
    public void setUp() {
        movieValidator = new MovieValidator();
        movieBuilder = new MovieBuilder();
        illegalCharacters = new String[]{"\\~", "\\#", "\\@", "\\*", "\\+", "\\%", "\\<", "\\>", "\\[", "\\]", "\\|", "\"", "\\_", "\\^", "\\£", "\\$", "\\€", "\\´"};

        constraintValidatorContextMock = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilderMock = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilderMock);
        when(constraintViolationBuilderMock.addConstraintViolation()).thenReturn(constraintValidatorContextMock);
    }

    @Test
    public void isValid_IdIsNull_ShouldReturnFalse() {
        Movie movie = movieBuilder.id(null).build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_IdIsEmpty_ShouldReturnFalse() {
        Movie movie = movieBuilder.id("").build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_IdIsTooLong_ShouldReturnFalse() {
        Movie movie = movieBuilder.id(getRandomString(ID_MAX_LENGTH + 1)).build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_IdContainsIllegalCharacters_ShouldReturnFalse() {
        for (String illegalCharacter : illegalCharacters) {
            Movie movie = movieBuilder.id(illegalCharacter).build();
            assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
        }
    }

    @Test
    public void isValid_NameIsNull_ShouldReturnFalse() {
        Movie movie = movieBuilder.name(null).build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_NameIsEmpty_ShouldReturnFalse() {
        Movie movie = movieBuilder.name("").build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_NameIsTooLong_ShouldReturnFalse() {
        Movie movie = movieBuilder.name(getRandomString(NAME_MAX_LENGTH + 1)).build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_NameContainsIllegalCharacters_ShouldReturnFalse() {
        for (String illegalCharacter : illegalCharacters) {
            Movie movie = movieBuilder.name(illegalCharacter).build();
            assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
        }
    }

    @Test
    public void isValid_ImdbIdIsTooLong_ShouldReturnFalse() {
        Movie movie = movieBuilder.imdbId(getRandomString(IMDB_ID_MAX_LENGTH + 1)).build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_ImdbIdContainsIllegalCharacters_ShouldReturnFalse() {
        for (String illegalCharacter : illegalCharacters) {
            Movie movie = movieBuilder.imdbId(illegalCharacter).build();
            assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
        }
    }

    @Test
    public void isValid_TaglineIsTooLong_ShouldReturnFalse() {
        Movie movie = movieBuilder.tagline(getRandomString(TAGLINE_MAX_LENGTH + 1)).build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_TaglineContainsIllegalCharacters_ShouldReturnFalse() {
        for (String illegalCharacter : illegalCharacters) {
            Movie movie = movieBuilder.tagline(illegalCharacter).build();
            assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
        }
    }

    @Test
    public void isValid_OverviewIsTooLong_ShouldReturnFalse() {
        Movie movie = movieBuilder.overview(getRandomString(OVERVIEW_MAX_LENGTH + 1)).build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_OverviewContainsIllegalCharacters_ShouldReturnFalse() {
        for (String illegalCharacter : illegalCharacters) {
            Movie movie = movieBuilder.overview(illegalCharacter).build();
            assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
        }
    }

    @Test
    public void isValid_InvalidGenre_ShouldReturnFalse() throws Exception {
        List<Genre> genres = new ArrayList<>();
        genres.add(null);
        Movie movie = movieBuilder.genres(genres).build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_VideoFilePathIsTooLong_ShouldReturnFalse() {
        File file = new File(getRandomString(VIDEO_FILE_PATH_MAX_LENGTH + 1));
        Movie movie = movieBuilder.videoFile(file).build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_VideoFilePathContainsIllegalCharacters_ShouldReturnFalse() {
        for (String illegalCharacter : illegalCharacters) {
            Movie movie = movieBuilder.videoFile(new File(illegalCharacter)).build();
            assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
        }
    }

    @Test
    public void isValid_PosterImageFilePathIsTooLong_ShouldReturnFalse() {
        File file = new File(getRandomString(POSTER_IMAGE_FILE_PATH_MAX_LENGTH + 1));
        Movie movie = movieBuilder.posterImageFile(file).build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_PosterImageFilePathContainsIllegalCharacters_ShouldReturnFalse() {
        for (String illegalCharacter : illegalCharacters) {
            Movie movie = movieBuilder.posterImageFile(new File(illegalCharacter)).build();
            assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
        }
    }

    @Test
    public void isValid_BackdropImageFilePathIsTooLong_ShouldReturnFalse() {
        File file = new File(getRandomString(BACKDROP_IMAGE_FILE_PATH_MAX_LENGTH + 1));
        Movie movie = movieBuilder.backdropImageFile(file).build();
        assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
    }

    @Test
    public void isValid_BackdropImageFilePathContainsIllegalCharacters_ShouldReturnFalse() {
        for (String illegalCharacter : illegalCharacters) {
            Movie movie = movieBuilder.backdropImageFile(new File(illegalCharacter)).build();
            assertFalse(movieValidator.isValid(movie, constraintValidatorContextMock));
        }
    }
}
