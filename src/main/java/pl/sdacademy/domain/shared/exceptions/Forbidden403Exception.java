package pl.sdacademy.domain.shared.exceptions;

import pl.sdacademy.domain.shared.ApiStatus;

public class Forbidden403Exception extends BaseApiException {
    public Forbidden403Exception(ApiStatus apiStatus) {
        super(apiStatus);
    }
}
