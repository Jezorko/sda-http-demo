package pl.sdacademy.domain.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;

@Getter
@ToString
@RequiredArgsConstructor
public class FieldValidationError {

    private final String field;

    private final String message;

    private final String code;

    public FieldValidationError(FieldError error) {
        this(error.getField(), error.getDefaultMessage(), error.getCode());
    }

    public FieldValidationError(ConstraintViolation<?> violation) {
        this(violation.getPropertyPath()
                      .toString(),
             violation.getMessage(),
             violation.getConstraintDescriptor()
                      .getAnnotation()
                      .annotationType()
                      .getSimpleName());
    }
}
