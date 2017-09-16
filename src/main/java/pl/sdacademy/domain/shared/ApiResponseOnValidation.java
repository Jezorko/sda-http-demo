package pl.sdacademy.domain.shared;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.util.Set;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@Getter
public class ApiResponseOnValidation extends ApiResponseOnException {

    @JsonSerialize(include = NON_NULL)
    private final Set<FieldValidationError> validationData;

    public ApiResponseOnValidation(ApiStatus status, String description, Set<FieldValidationError> validationData) {
        super(status, description);
        this.validationData = validationData;
    }
}
