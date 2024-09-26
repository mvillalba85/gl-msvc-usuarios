package com.mvillalba.msvc_usuarios.exceptions;

import com.mvillalba.msvc_usuarios.dto.ErrorDTO;
import com.mvillalba.msvc_usuarios.dto.response.ResponseRest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseRest> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResponseRest responseError = new ResponseRest();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            Integer codigo = HttpStatus.BAD_REQUEST.value();

            ErrorDTO errorDetail = new ErrorDTO(Timestamp.from(Instant.now()), codigo, errorMessage);
            responseError.addError(errorDetail);
        });

        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseRest> handleRuntimeException(RuntimeException ex) {
        ResponseRest responseError = new ResponseRest();

        ErrorDTO errorDTO = new ErrorDTO(
                Timestamp.from(Instant.now()),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        responseError.addError(errorDTO);
        return ResponseEntity.badRequest().body(responseError);
    }
}
