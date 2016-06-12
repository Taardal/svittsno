package no.svitts.core.constraint;

import no.svitts.core.constraint.validator.MovieValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = MovieValidator.class)
public @interface ValidMovie {

    String message() default "Could not validate movie.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
