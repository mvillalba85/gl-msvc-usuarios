package com.mvillalba.msvc_usuarios.controllers;

import com.mvillalba.msvc_usuarios.dto.ErrorDTO;
import com.mvillalba.msvc_usuarios.dto.ResponseDTO;
import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.dto.authentication.AuthenticationRequestDTO;
import com.mvillalba.msvc_usuarios.dto.authentication.AuthenticationResponseDTO;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.security.JWTUtil;
import com.mvillalba.msvc_usuarios.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    @GetMapping("/login/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
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

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> createToken(@RequestHeader("Authorization") String token){
        try{
            final UserDTO userDTO = userService.findByEmail(token);
            if(userDTO != null){
                return new ResponseEntity<>(new ResponseDTO(true, userDTO), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseDTO(false, null), HttpStatus.NOT_FOUND);
        }catch (BadCredentialsException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
}
