package pl.sdacademy.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.sdacademy.domain.shared.ApiResponseOnException;
import pl.sdacademy.domain.shared.ApiStatus;
import pl.sdacademy.domain.shared.ApiStatusMessageResolver;
import pl.sdacademy.domain.shared.exceptions.BaseApiException;

import static pl.sdacademy.domain.shared.ApiStatus.INTERNAL_SERVER_ERROR;
import static rx.Observable.just;
import static rx.Observable.zip;

@Slf4j
@Component
@RequiredArgsConstructor
class ApiResponseBuilder {
    private final ApiStatusMessageResolver apiStatusMessageResolver;

    ResponseEntity<ApiResponseOnException> buildServerError() {
        return buildFrom(INTERNAL_SERVER_ERROR).wrap(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    ResponseEntity<ApiResponseOnException> buildFrom(BaseApiException exception) {
        return buildFrom(exception.getApiStatus()).wrap(exception.getHttpStatus());
    }

    private ApiResponseOnException buildFrom(ApiStatus apiStatus) {
        return zip(just(apiStatus),
                   apiStatusMessageResolver.resolveMessageFor(apiStatus),
                   ApiResponseOnException::new)
                .toBlocking()
                .single();
    }
}
