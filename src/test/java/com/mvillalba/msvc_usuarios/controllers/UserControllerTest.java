package com.mvillalba.msvc_usuarios.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.dto.response.UserResponseRest;
import com.mvillalba.msvc_usuarios.entities.Phone;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.exceptions.UserException;
import com.mvillalba.msvc_usuarios.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    private UserServiceImpl userService;

    private User user;

    private UserDTO userDTO;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        Phone phone = new Phone(5L, 342, "54");

        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("Password123");
        user.setActive(true);
        user.addPhone(phone);

        userDTO = new UserDTO();
        userDTO.setName("Test User");
        userDTO.setId(UUID.randomUUID());
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("Password123");
        userDTO.setIsActive(true);
        userDTO.setToken("Test Token");
        userDTO.setCreated("2023-09-27T00:00:00");
        userDTO.setLastLogin("");

        Phone phone1 = new Phone(5L, 342, "54");
        userDTO.addPhone(phone1);

    }

    @Test
    void testSignUpSuccess() throws Exception {
        UserResponseRest responseRest = new UserResponseRest();
        responseRest.setUserDTO(userDTO);

        when(userService.signUp(any(User.class))).thenReturn(userDTO);
        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userDTO.token").value("Test Token"))
                .andExpect(jsonPath("$.userDTO.email").value("test@example.com"));

        verify(userService, times(1)).signUp(any(User.class));
    }

    @Test
    void testSignUpUserException() throws Exception {
        String errorMessage = "Ya existe un usuario con el mismo email";

        when(userService.signUp(any(User.class))).thenThrow(new UserException(errorMessage));

        mockMvc.perform(post("/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].detail").value(errorMessage));

        verify(userService, times(1)).signUp(any(User.class));
    }

    @Test
    void testSignUpUnexpectedException() throws Exception {
        String errorMessage = "Error inesperado: No fue posible crear el Usuario";

        when(userService.signUp(any(User.class))).thenThrow(new RuntimeException(errorMessage));

        mockMvc.perform(post("/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error[0].detail").value(errorMessage));

        verify(userService, times(1)).signUp(any(User.class));
    }


    @Test
    void testLoginSuccess() throws Exception {
        String token = "tokenOk";
        when(userService.login(token)).thenReturn(userDTO);

        mockMvc.perform(post("/users/login")
                .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userDTO").exists());
    }

    @Test
    public void testLoginUserException() throws Exception {
        String token = "invalid_token";

        when(userService.login(token)).thenThrow(new UserException("Usuario no activo"));

        mockMvc.perform(post("/users/login")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error[0].detail").value("Usuario no activo"));
    }

    @Test
    public void testLoginBadCredentialsException() throws Exception {
        String token = "bad_credentials_token";

        when(userService.login(token)).thenThrow(new BadCredentialsException("Usuario no encontrado"));

        mockMvc.perform(post("/users/login")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].detail").value("Usuario no encontrado"));
    }

    @Test
    public void testLoginUnexpectedException() throws Exception {
        String token = "unexpected_error_token";

        when(userService.login(token)).thenThrow(new RuntimeException("Se produjo un error inesperado"));

        mockMvc.perform(post("/users/login")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error[0].detail").value("Se produjo un error inesperado"));
    }
}