package com.mvillalba.msvc_usuarios.controllers;

import com.mvillalba.msvc_usuarios.dto.ErrorDTO;
import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.dto.response.Response;
import com.mvillalba.msvc_usuarios.dto.response.ResponseError;
import com.mvillalba.msvc_usuarios.dto.response.ResponseUser;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.services.LoginService;
import com.mvillalba.msvc_usuarios.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @PostMapping("sign-up")
    public ResponseEntity<Response> createUser(@RequestBody User user) {
        try{
            UserDTO userDTO = userService.save(user);
//            return new ResponseEntity<>(new ResponseUser(userDTO), HttpStatus.CREATED);
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now(), 500, "Error inesperado: No fue posible crear el Usuario");
            return new ResponseEntity<>(new ResponseError(errorDTO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestHeader("Authorization") String token){
        try{
            UserDTO userDTO = loginService.login(token);
//            return new ResponseEntity<>(new ResponseUser(userDTO), HttpStatus.OK);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }catch (BadCredentialsException e){
            e.printStackTrace();
            ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now(), 403, e.getMessage());
            return new ResponseEntity<>(new ResponseError(errorDTO), HttpStatus.FORBIDDEN);
        }catch (Exception e){
            e.printStackTrace();
            ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now(), 500, "Se produjo un error inesperado");
            return new ResponseEntity<>(new ResponseError(errorDTO), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
