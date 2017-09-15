package pl.sdacademy.domain.shared;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@Value
@RequiredArgsConstructor
public class ApiResponseOnException {
    private final ApiStatus status;
    private final String description;

    @JsonSerialize(include = NON_NULL)
    private final String exceptionData;
}
