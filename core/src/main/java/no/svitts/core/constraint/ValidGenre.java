package no.svitts.core.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Retention(RUNTIME)
@Constraint(validatedBy = GenreValidator.class)
public @interface ValidGenre {

    String message() default "Could not validate genre.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
