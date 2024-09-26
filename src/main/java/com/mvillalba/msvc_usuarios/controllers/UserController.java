package com.mvillalba.msvc_usuarios.controllers;

import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.dto.ErrorDTO;
import com.mvillalba.msvc_usuarios.dto.response.UserResponseRest;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.exceptions.UserException;
import com.mvillalba.msvc_usuarios.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Instant;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Validated
    @PostMapping("sign-up")
    public ResponseEntity<UserResponseRest> signUp(@Valid @RequestBody User user) {
        UserResponseRest responseRest = new UserResponseRest();
        try{
            UserDTO userDTO = userService.signUp(user);
            responseRest.setUserDTO(userDTO);
            return new ResponseEntity<>(responseRest, HttpStatus.CREATED);
        }catch (UserException e){
            e.printStackTrace();
            ErrorDTO errorDTO = new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), e.getMessage());
            responseRest.addError(errorDTO);
            return new ResponseEntity<>(responseRest, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            String msgError = "Error inesperado: No fue posible crear el Usuario";
            ErrorDTO errorDTO = new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.INTERNAL_SERVER_ERROR.value(), msgError);
            responseRest.addError(errorDTO);
            return new ResponseEntity<>(responseRest, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseRest> login(@RequestHeader("Authorization") String token){
        UserResponseRest responseRest = new UserResponseRest();
        try{
            UserDTO userDTO = userService.login(token);
            responseRest.setUserDTO(userDTO);
            return new ResponseEntity<>(responseRest, HttpStatus.OK);
        }catch (BadCredentialsException e){
            e.printStackTrace();
            ErrorDTO errorDTO = new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), e.getMessage());
            responseRest.addError(errorDTO);
            return new ResponseEntity<>(responseRest, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
            String msgError = "Se produjo un error inesperado";
            ErrorDTO errorDTO = new ErrorDTO(Timestamp.from(Instant.now()), HttpStatus.INTERNAL_SERVER_ERROR.value(), msgError);
            responseRest.addError(errorDTO);
            return new ResponseEntity<>(responseRest, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
