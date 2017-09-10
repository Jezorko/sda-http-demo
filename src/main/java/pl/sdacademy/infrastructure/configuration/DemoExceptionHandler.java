package pl.sdacademy.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.sdacademy.domain.shared.ApiResponseOnException;
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception;
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class DemoExceptionHandler {

    private final ApiResponseBuilder apiResponseBuilder;

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseOnException> internalServer500(Exception exception) {
        log.error("Exception thrown", exception);
        return apiResponseBuilder.buildServerError();
    }

    @ExceptionHandler(BadRequest400Exception.class)
    public ResponseEntity<ApiResponseOnException> badRequest400(BadRequest400Exception exception) {
        return apiResponseBuilder.buildFrom(exception);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFound404Exception.class)
    public ResponseEntity<ApiResponseOnException> notFound404(NotFound404Exception exception) {
        return apiResponseBuilder.buildFrom(exception);
    }
}
