package no.svitts.core.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Constraint(validatedBy = NameValidator.class)
public @interface ValidName {

    String message() default "Could not validate name.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
