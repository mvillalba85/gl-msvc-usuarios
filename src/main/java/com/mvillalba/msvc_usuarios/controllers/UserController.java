package com.mvillalba.msvc_usuarios.controllers;

import com.mvillalba.msvc_usuarios.dto.ErrorDTO;
import com.mvillalba.msvc_usuarios.dto.ResponseDTO;
import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

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


    @PostMapping
    public ResponseEntity<ResponseDTO> createUser(@RequestBody User user) {
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

}
