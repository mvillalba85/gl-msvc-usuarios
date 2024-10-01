package com.mvillalba.msvc_usuarios.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.dto.response.ResponseRest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
//@WebMvcTest(UserController.class)
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
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
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
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
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
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
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
    void loginSuccess() {
    }
}