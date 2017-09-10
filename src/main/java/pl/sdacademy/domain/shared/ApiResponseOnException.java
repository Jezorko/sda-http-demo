package pl.sdacademy.domain.shared;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Value
@RequiredArgsConstructor
public class ApiResponseOnException {
    private final ApiStatus status;
    private final String description;

    public ResponseEntity<ApiResponseOnException> wrap(HttpStatus status) {
        return new ResponseEntity<>(this, status);
    }
}
