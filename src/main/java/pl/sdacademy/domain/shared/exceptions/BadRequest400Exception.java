package pl.sdacademy.domain.shared.exceptions;

import pl.sdacademy.domain.shared.ApiStatus;

public class BadRequest400Exception extends BaseApiException {
    public BadRequest400Exception(ApiStatus apiStatus) {
        super(apiStatus);
    }
}
