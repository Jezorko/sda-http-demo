package pl.sdacademy.domain.shared.exceptions;

import org.springframework.http.HttpStatus;
import pl.sdacademy.domain.shared.ApiStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class Forbidden403Exception extends BaseApiException {
    public Forbidden403Exception(ApiStatus apiStatus) {
        super(apiStatus);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return FORBIDDEN;
    }
}
