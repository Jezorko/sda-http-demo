package pl.sdacademy.domain.shared.validation;

import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.Email;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Null;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.validator.constraints.CompositionType.OR;

@Null
@Email
@Documented
@Retention(RUNTIME)
@ConstraintComposition(OR)
@Constraint(validatedBy = {})
@Target({FIELD, ANNOTATION_TYPE})
public @interface OptionalEmail {
    String message() default "{}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
