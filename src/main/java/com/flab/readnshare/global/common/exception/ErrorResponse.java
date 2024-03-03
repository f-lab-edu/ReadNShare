package com.flab.readnshare.global.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {

    private String code;
    private HttpStatus status;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationError> errors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError{
        private final String field;
        private final String message;
        public static ValidationError of(final FieldError fieldError){
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }

    public ErrorResponse(ErrorCode errorCode){
       this.code = errorCode.toString();
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }

    public ErrorResponse(BindException ex, ErrorCode errorCode){
        List<ErrorResponse.ValidationError> validationErrorList = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        this.code = errorCode.toString();
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.errors = validationErrorList;
    }
}
