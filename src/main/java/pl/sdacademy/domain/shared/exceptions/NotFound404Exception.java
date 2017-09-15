package pl.sdacademy.domain.shared.exceptions;

import pl.sdacademy.domain.shared.ApiStatus;

public class NotFound404Exception extends BaseApiException {
    public NotFound404Exception(ApiStatus apiStatus) {
        super(apiStatus);
    }
}
