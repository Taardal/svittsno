package no.svitts.core.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = IdValidator.class)
public @interface ValidId {

    String message() default "Could not validate ID.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
