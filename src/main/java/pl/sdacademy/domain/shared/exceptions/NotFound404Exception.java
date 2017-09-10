package pl.sdacademy.domain.shared.exceptions;

import org.springframework.http.HttpStatus;
import pl.sdacademy.domain.shared.ApiStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFound404Exception extends BaseApiException {
    public NotFound404Exception(ApiStatus apiStatus) {
        super(apiStatus);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return NOT_FOUND;
    }
}
