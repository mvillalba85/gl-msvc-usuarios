package com.mvillalba.msvc_usuarios.exceptions;

import com.mvillalba.msvc_usuarios.dto.response.ErrorDetails;
import com.mvillalba.msvc_usuarios.dto.response.Response;
import com.mvillalba.msvc_usuarios.dto.response.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResponseError responseError = new ResponseError();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            Integer codigo = HttpStatus.BAD_REQUEST.value();

            ErrorDetails errorDetail = new ErrorDetails(Timestamp.from(Instant.now()), codigo, fieldName + ": " + errorMessage);
            responseError.addError(errorDetail);
        });

        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseError> handleRuntimeException(RuntimeException ex) {
        ResponseError responseError = new ResponseError();

        ErrorDetails errorDetails = new ErrorDetails(
                Timestamp.from(Instant.now()),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        responseError.addError(errorDetails);
        return ResponseEntity.badRequest().body(responseError);
    }
}
