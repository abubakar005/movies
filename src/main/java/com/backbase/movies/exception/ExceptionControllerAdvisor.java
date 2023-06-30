package com.backbase.movies.exception;

import com.backbase.movies.dto.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This is the Controller Advisor which will catch the exceptions and will return as a response body with error message
 * */

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvisor {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UserUnauthorizedException.class)
    public ResponseEntity<ErrorInfo> handleUnauthorizedException(UserUnauthorizedException exception) {
        log.error("Validation exception occurred with errors: {}", exception.getMessage());
        return new ResponseEntity<>(new ErrorInfo(exception.getCode(), exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> handleNotFoundException(NotFoundException exception) {
        log.error("Exception occurred with errors: {}", exception.getMsg());
        return new ResponseEntity<>(new ErrorInfo(exception.getCode(), exception.getMsg()), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorInfo> handleServiceUnAvailableException(ServiceUnavailableException exception) {
        log.error("Exception occurred with errors: {}", exception.getMsg());
        return new ResponseEntity<>(new ErrorInfo(exception.getCode(), exception.getMsg()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ErrorInfo> handleDataConflictException(DataConflictException exception) {
        log.error("Exception occurred with errors: {}", exception.getMsg());
        return new ResponseEntity<>(new ErrorInfo(exception.getCode(), exception.getMsg()), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorInfo> handleGeneralException(GeneralException exception) {
        log.error("Exception occurred with errors: {}", exception.getMsg());
        return new ResponseEntity<>(new ErrorInfo(exception.getCode(), exception.getMsg()), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
