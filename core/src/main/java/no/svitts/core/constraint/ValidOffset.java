package no.svitts.core.constraint;

import no.svitts.core.constraint.validator.OffsetValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
@Constraint(validatedBy = OffsetValidator.class)
public @interface ValidOffset {

    String message() default "Could not validate offset.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
