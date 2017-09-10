package pl.sdacademy.domain.shared.exceptions;

import org.springframework.http.HttpStatus;
import pl.sdacademy.domain.shared.ApiStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BadRequest400Exception extends BaseApiException {
    public BadRequest400Exception(ApiStatus apiStatus) {
        super(apiStatus);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return BAD_REQUEST;
    }
}
