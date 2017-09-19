package pl.sdacademy.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import pl.sdacademy.domain.shared.ApiResponseOnException;
import pl.sdacademy.domain.shared.ApiResponseOnValidation;
import pl.sdacademy.domain.shared.exceptions.BadRequest400Exception;
import pl.sdacademy.domain.shared.exceptions.Forbidden403Exception;
import pl.sdacademy.domain.shared.exceptions.InternalServer500Exception;
import pl.sdacademy.domain.shared.exceptions.NotFound404Exception;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static pl.sdacademy.domain.shared.ApiStatus.*;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class DemoExceptionHandler {

    private final ApiResponseBuilder apiResponseBuilder;

    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponseOnException internalServer500(Exception exception) {
        return apiResponseBuilder.buildServerError();
    }

    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServer500Exception.class)
    public ApiResponseOnException internalServer500(InternalServer500Exception exception) {
        return apiResponseBuilder.buildFrom(exception);
    }

    @ResponseBody
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponseOnException multipartError(MethodArgumentTypeMismatchException exception) {
        return apiResponseBuilder.buildFrom(INVALID_MULTIPART_REQUEST);
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    public ApiResponseOnException multipartError(MultipartException exception) {
        return apiResponseBuilder.buildFrom(INVALID_MULTIPART_REQUEST);
    }

    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponseOnException invalidMethod(NoHandlerFoundException exception) {
        return apiResponseBuilder.buildFrom(INVALID_ENDPOINT);
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiResponseOnException invalidMediaType(HttpMediaTypeNotSupportedException exception) {
        return apiResponseBuilder.buildFrom(MEDIA_TYPE_NOT_SUPPORTED);
    }

    @ResponseBody
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponseOnException messageNotReadable(HttpMessageNotReadableException exception) {
        return apiResponseBuilder.buildFrom(MESSAGE_NOT_READABLE);
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequest400Exception.class)
    public ApiResponseOnException badRequest400(BadRequest400Exception exception) {
        return apiResponseBuilder.buildFrom(exception);
    }

    @ResponseBody
    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(Forbidden403Exception.class)
    public ApiResponseOnException forbidden403(Forbidden403Exception exception) {
        return apiResponseBuilder.buildFrom(exception);
    }

    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFound404Exception.class)
    public ApiResponseOnException notFound404(NotFound404Exception exception) {
        return apiResponseBuilder.buildFrom(exception);
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponseOnValidation methodArgumentValidation(MethodArgumentNotValidException exception) {
        return apiResponseBuilder.buildFrom(exception);
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponseOnValidation constraintViolation(ConstraintViolationException exception) {
        return apiResponseBuilder.buildFrom(exception);
    }

}
