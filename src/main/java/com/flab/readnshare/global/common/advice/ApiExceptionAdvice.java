package com.flab.readnshare.global.common.advice;

import com.flab.readnshare.global.common.exception.*;
import com.flab.readnshare.global.common.exception.RestTemplateResponseErrorHandler.RestCallException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ErrorResponse response = new ErrorResponse(ex, ErrorCode.INVALID_INPUT_PARAMETER);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<Object> handleServerError(Exception ex) {
        ErrorResponse response = new ErrorResponse(ErrorCode.SERVER_ERROR);
        return new ResponseEntity<>(response, ErrorCode.SERVER_ERROR.getStatus());
    }

    @ExceptionHandler(BaseDomainException.class)
    public ResponseEntity<ErrorResponse> baseDomainExceptionHandler(BaseDomainException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(RestCallException.class)
    public ResponseEntity<ErrorResponse> restCallExceptionHandler(RestCallException ex) {
        ErrorResponse response = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(response, ex.getStatus());
    }

}
