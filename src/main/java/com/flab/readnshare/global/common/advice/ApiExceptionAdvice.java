package com.flab.readnshare.global.common.advice;

import com.flab.readnshare.global.common.exception.*;
import com.flab.readnshare.global.common.exception.RestTemplateResponseErrorHandler.RestCallException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(MemberException.class)
    public ResponseEntity memberExceptionHandler(MemberException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity authExceptionHandler(AuthException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, ex.getErrorCode().getStatus());
    }

    @ExceptionHandler(RestCallException.class)
    public ResponseEntity restCallExceptionHandler(RestCallException ex) {
        ErrorResponse response = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(response, ex.getStatus());
    }

}
