package com.mvillalba.msvc_usuarios.controllers;

import com.mvillalba.msvc_usuarios.dto.ErrorDTO;
import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.dto.response.ErrorDetails;
import com.mvillalba.msvc_usuarios.dto.response.Response;
import com.mvillalba.msvc_usuarios.dto.response.ResponseError;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.exceptions.UserException;
import com.mvillalba.msvc_usuarios.services.LoginService;
import com.mvillalba.msvc_usuarios.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Validated
    @PostMapping("sign-up")
    public ResponseEntity<Response> signUp(@Valid @RequestBody User user, BindingResult result) {
        try{
            if(result.hasErrors()){
                return new ResponseEntity<>(getErrorDetails(result), HttpStatus.BAD_REQUEST);
            }
            UserDTO userDTO = userService.signUp(user);
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        }catch (UserException e){
            e.printStackTrace();
            ResponseError responseError = new ResponseError();
            ErrorDetails errorDetails = new ErrorDetails(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), e.getMessage());
            responseError.addError(errorDetails);
            return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            ResponseError responseError = new ResponseError();
            String msgError = "Error inesperado: No fue posible crear el Usuario";
            ErrorDetails errorDetails = new ErrorDetails(Timestamp.from(Instant.now()), HttpStatus.INTERNAL_SERVER_ERROR.value(), msgError);
            responseError.addError(errorDetails);
            return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseError getErrorDetails(BindingResult result) {
        ResponseError responseError = new ResponseError();
        result.getFieldErrors().stream()
                .map(error -> new ErrorDetails(
                        Timestamp.from(Instant.now()),
                        HttpStatus.BAD_REQUEST.value(),
                        error.getDefaultMessage()))
                .forEach(errorDetails -> responseError.addError(errorDetails));

        return responseError;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String token){
        try{
            UserDTO userDTO = loginService.login(token);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }catch (BadCredentialsException e){
            e.printStackTrace();
            ResponseError responseError = new ResponseError();
            ErrorDetails errorDetails = new ErrorDetails(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), e.getMessage());
            responseError.addError(errorDetails);
            return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
            ResponseError responseError = new ResponseError();
            String msgError = "Se produjo un error inesperado";
            ErrorDetails errorDetails = new ErrorDetails(Timestamp.from(Instant.now()), HttpStatus.INTERNAL_SERVER_ERROR.value(), msgError);
            responseError.addError(errorDetails);
            return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
