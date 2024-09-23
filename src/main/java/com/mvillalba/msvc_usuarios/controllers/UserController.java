package com.mvillalba.msvc_usuarios.controllers;

import com.mvillalba.msvc_usuarios.dto.ErrorDTO;
import com.mvillalba.msvc_usuarios.dto.ResponseDTO;
import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.dto.authentication.AuthenticationRequestDTO;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.services.UserService;
import com.mvillalba.msvc_usuarios.services.impl.UserDetailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        final UserDTO userDTO = userService.findById(id);
        return userDTO != null ? ResponseEntity.ok(userDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        final List<UserDTO> userDTOS = userService.findAll();
        return userDTOS != null ? ResponseEntity.ok(userDTOS) : ResponseEntity.noContent().build();
    }


    @PostMapping("sign-up")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody User user) {
        LOGGER.warn("Entro al registro del usuario");
        ResponseDTO resp = new ResponseDTO();
        try{
            UserDTO userDTO = userService.save(user);
            return new ResponseEntity<>(new ResponseDTO(true, userDTO), HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            final ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now(), 500, "Error inesperado: No fue posible crear el Usuario");
            resp.setSuccess(false);
            resp.getErrores().add(errorDTO);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping()
    public ResponseEntity<ResponseDTO> createUser(@RequestBody AuthenticationRequestDTO request) {
        LOGGER.warn("Entro al registro del usuario");
        ResponseDTO resp = new ResponseDTO();
        try{
            User user = new User();
            user.setName(request.getUsername());
            user.setPassword(request.getPassword());
            UserDTO userDTO = userService.save(user);
            return new ResponseEntity<>(new ResponseDTO(true, userDTO), HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
            final ErrorDTO errorDTO = new ErrorDTO(LocalDateTime.now(), 500, "Error inesperado: No fue posible crear el Usuario");
            resp.setSuccess(false);
            resp.getErrores().add(errorDTO);
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


}
