package pl.sdacademy.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.sdacademy.domain.shared.ApiResponseOnException;
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception;
import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception;
import pl.sdacademy.domain.shared.exceptions.InternalServer500Exception;
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception;

import static org.springframework.http.HttpStatus.*;
import static pl.sdacademy.domain.shared.ApiStatus.MEDIA_TYPE_NOT_SUPPORTED;
import static pl.sdacademy.domain.shared.ApiStatus.MESSAGE_NOT_READABLE;

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

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponseOnException> invalidMediaType(HttpMediaTypeNotSupportedException exception) {
        log.info("", exception);
        return apiResponseBuilder.buildFrom(exception, MEDIA_TYPE_NOT_SUPPORTED, BAD_REQUEST);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseOnException> messageNotReadable(HttpMessageNotReadableException exception) {
        log.info("", exception);
        return apiResponseBuilder.buildFrom(MESSAGE_NOT_READABLE, BAD_REQUEST);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequest400Exception.class)
    public ResponseEntity<ApiResponseOnException> badRequest400(BadRequest400Exception exception) {
        log.info("", exception);
        return apiResponseBuilder.buildFrom(exception);
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(Forbidden403Exception.class)
    public ResponseEntity<ApiResponseOnException> forbidden403(Forbidden403Exception exception) {
        log.info("", exception);
        return apiResponseBuilder.buildFrom(exception);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFound404Exception.class)
    public ResponseEntity<ApiResponseOnException> notFound404(NotFound404Exception exception) {
        log.info("", exception);
        return apiResponseBuilder.buildFrom(exception);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServer500Exception.class)
    public ResponseEntity<ApiResponseOnException> internalServer500(InternalServer500Exception exception) {
        log.error("Exception thrown", exception);
        return apiResponseBuilder.buildFrom(exception);
    }
}
