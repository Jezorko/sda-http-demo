package pl.sdacademy.domain.shared.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import pl.sdacademy.domain.shared.ApiStatus;

@Getter
@RequiredArgsConstructor
public abstract class BaseApiException extends RuntimeException {
    private final ApiStatus apiStatus;

    public abstract HttpStatus getHttpStatus();
}
