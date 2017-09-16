package pl.sdacademy.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.sdacademy.domain.shared.*;
import pl.sdacademy.domain.shared.exceptions.BaseApiException;
import rx.functions.Func2;

import javax.validation.ConstraintViolationException;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static pl.sdacademy.domain.shared.ApiStatus.INTERNAL_SERVER_ERROR;
import static pl.sdacademy.domain.shared.ApiStatus.VALIDATION_ERROR;
import static rx.Observable.just;
import static rx.Observable.zip;

@Slf4j
@Component
@RequiredArgsConstructor
class ApiResponseBuilder {
    private final ApiStatusMessageResolver apiStatusMessageResolver;

    ApiResponseOnException buildServerError() {
        return buildFrom(INTERNAL_SERVER_ERROR);
    }

    ApiResponseOnException buildFrom(BaseApiException exception) {
        return buildFrom(exception.getApiStatus());
    }

    ApiResponseOnValidation buildFrom(MethodArgumentNotValidException exception) {
        return buildFrom(exception.getBindingResult()
                                  .getFieldErrors()
                                  .stream()
                                  .map(FieldValidationError::new)
                                  .collect(toSet()));
    }

    ApiResponseOnValidation buildFrom(ConstraintViolationException exception) {
        return buildFrom(exception.getConstraintViolations()
                                  .stream()
                                  .map(FieldValidationError::new)
                                  .collect(toSet()));
    }

    ApiResponseOnException buildFrom(ApiStatus apiStatus) {
        return buildFrom(apiStatus, ApiResponseOnException::new);
    }

    private ApiResponseOnValidation buildFrom(Set<FieldValidationError> validationErrors) {
        return buildFrom(VALIDATION_ERROR, (apiStatus, description) -> new ApiResponseOnValidation(apiStatus, description, validationErrors));
    }

    private <T extends ApiResponseOnException> T buildFrom(ApiStatus apiStatus, Func2<ApiStatus, String, T> responseConstructor) {
        return zip(just(apiStatus),
                   apiStatusMessageResolver.resolveMessageFor(apiStatus),
                   responseConstructor)
                .toBlocking()
                .single();
    }
}
