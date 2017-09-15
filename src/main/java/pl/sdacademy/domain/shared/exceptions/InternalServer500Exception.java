package pl.sdacademy.domain.shared.exceptions;

import pl.sdacademy.domain.shared.ApiStatus;

public class InternalServer500Exception extends BaseApiException {
    public InternalServer500Exception(ApiStatus apiStatus) {
        super(apiStatus);
    }
}
