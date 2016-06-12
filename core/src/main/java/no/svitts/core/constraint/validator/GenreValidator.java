package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.ValidGenre;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GenreValidator extends CoreValidator implements ConstraintValidator<ValidGenre, String> {

    static final int GENRE_MAX_LENGTH = 255;

    @Override
    public void initialize(ValidGenre validGenre) {

    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        boolean tooLong = string.length() > GENRE_MAX_LENGTH;
        boolean containsIllegalCharacters = containsIllegalCharacters(string);
        if (tooLong) {
            addConstraintViolation("Genre was too long.", constraintValidatorContext);
        }
        if (containsIllegalCharacters) {
            addConstraintViolation("Genre contained illegal characters.", constraintValidatorContext);
        }
        return !tooLong && !containsIllegalCharacters;

    }
}
