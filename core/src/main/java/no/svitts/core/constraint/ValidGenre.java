package no.svitts.core.constraint;

import no.svitts.core.constraint.validator.GenreValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = GenreValidator.class)
public @interface ValidGenre {

    String message() default "Could not validate genre.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
