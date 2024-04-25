package com.example.university.web.advice;

import com.example.university.exception.*;
import com.example.university.exception.service.ExceptionLoggingService;
import com.example.university.web.constants.ErrorMessageConstants;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.Instant;

@RestControllerAdvice
@AllArgsConstructor
public class ExceptionHandlingRestControllerAdvice {

    private final ExceptionLoggingService exceptionLoggingService;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        exceptionLoggingService.logRequestError(request, exception);

        String message = ErrorMessageConstants.RESOURCE_NOT_FOUND_ERROR_MESSAGE;

        return exceptionResponseEntity(message, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException exception, HttpServletRequest request) {
        exceptionLoggingService.logRequestError(request, exception);

        String message = exception.getMessage();

        return exceptionResponseEntity(message, HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorMessage> handleExpiredJwtException(UserNotFoundException exception, HttpServletRequest request) {
        exceptionLoggingService.logRequestError(request, exception);

        String message = exception.getMessage();

        return exceptionResponseEntity(message, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException exception, HttpServletRequest request) {
        exceptionLoggingService.logRequestError(request, exception);

        String message = exception.getMessage();

        return exceptionResponseEntity(message, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        exceptionLoggingService.logRequestError(request, exception);

        String message = ErrorMessageConstants.ARGUMENT_NOT_VALID_ERROR_MESSAGE;

        return exceptionResponseEntity(message, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        exceptionLoggingService.logRequestError(request, exception);

        String message = ErrorMessageConstants.ARGUMENT_TYPE_MISMATCH_ERROR_MESSAGE;

        return exceptionResponseEntity(message, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException exception, HttpServletRequest request) {
        exceptionLoggingService.logRequestError(request, exception);

        String message = exception.getMessage();

        return exceptionResponseEntity(message, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
        exceptionLoggingService.logRequestError(request, exception);

        String message = ErrorMessageConstants.ILLEGAL_ARGUMENT_ERROR_MESSAGE;

        return exceptionResponseEntity(message, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        exceptionLoggingService.logRequestError(request, exception);

        String message = ErrorMessageConstants.MESSAGE_NOT_READABLE_ERROR_MESSAGE;

        return exceptionResponseEntity(message, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorMessage> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception, HttpServletRequest request) {
        exceptionLoggingService.logRequestError(request, exception);

        String message = ErrorMessageConstants.MEDIA_TYPE_NOT_SUPPORTED_ERROR_MESSAGE;

        return exceptionResponseEntity(message, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception, HttpServletRequest request) {
        exceptionLoggingService.logRequestError(request, exception);

        String resolvedMessage = ErrorMessageConstants.EXCEPTION_ERROR_MESSAGE;

        return exceptionResponseEntity(resolvedMessage, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorMessage> exceptionResponseEntity(String exceptionMessage, HttpStatus httpStatus, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        ErrorMessage errorResponse = new ErrorMessage(httpStatus.value(), Instant.now(), exceptionMessage, requestURI);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
