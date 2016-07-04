package no.svitts.core.constraint;

import no.svitts.core.constraint.validator.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER, FIELD, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface ValidDate {

    String message() default "Could not validate date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
