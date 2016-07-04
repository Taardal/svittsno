package no.svitts.core.constraint.validator;

import no.svitts.core.constraint.ValidDate;
import no.svitts.core.date.ReleaseDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator extends CoreValidator implements ConstraintValidator<ValidDate, Long> {

    @Override
    public void initialize(ValidDate validDate) {

    }

    @Override
    public boolean isValid(Long time, ConstraintValidatorContext constraintValidatorContext) {
        ReleaseDate releaseDate = new ReleaseDate(1900, 1, 1);
        if (time >= releaseDate.getTime()) {
            return true;
        } else {
            addConstraintViolation("Date is too early. Must be after 01.01.1900.", constraintValidatorContext);
            return false;
        }
    }
}
