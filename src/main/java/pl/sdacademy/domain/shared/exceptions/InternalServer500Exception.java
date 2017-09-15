package pl.sdacademy.domain.shared.exceptions;

import org.springframework.http.HttpStatus;
import pl.sdacademy.domain.shared.ApiStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class InternalServer500Exception extends BaseApiException {
    public InternalServer500Exception(ApiStatus apiStatus) {
        super(apiStatus);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return INTERNAL_SERVER_ERROR;
    }

}
