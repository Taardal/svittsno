package no.svitts.core.validation;

import no.svitts.core.movie.Movie;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdValidator implements ConstraintValidator<ValidId, String> {

    @Override
    public void initialize(ValidId validId) {

    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        return id != null && !id.equals("") && id.length() <= Movie.ID_MAX_LENGTH;
    }

}
