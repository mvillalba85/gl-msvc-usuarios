package com.mvillalba.msvc_usuarios.services.impl;

import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.mapper.util.UtilMapConverter;
import com.mvillalba.msvc_usuarios.repositories.UserRepository;
import com.mvillalba.msvc_usuarios.security.JWTUtil;
import com.mvillalba.msvc_usuarios.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UtilMapConverter utilMapConverter;

    private User user;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("Password123");
        user.setActive(true);

        userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreated("dd/mm/yyyy hh:mm:ss");
        userDTO.setIsActive(user.getActive());
        userDTO.setCreated(utilMapConverter.localDateTimeToString(LocalDateTime.now()));

    }

    @Test
    void testSignUpSuccess() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("generated_token");

        UserDTO userDTO = userService.signUp(user);

        assertNotNull(userDTO);
        assertEquals(user.getEmail(), userDTO.getEmail());
        verify(userRepository, times(1)).save(user);
        verify(jwtUtil, times(1)).generateToken(user.getEmail());
    }

    @Test
    void testLoginSuccess() {
        when(jwtUtil.validateToken("valid_token", any(UserDetails.class))).thenReturn(true);
        UserDTO result = userService.login("valid_token");

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(jwtUtil, times(1)).validateToken("valid_token", any(UserDetails.class));
    }

    @Test
    void testLoginInvalidToken() {
        when("tokenInvalid".replace("Bearer ", "")).thenReturn("invalid_jwtToken");
        when(jwtUtil.extractUsername("invalid_jwtToken")).thenReturn("invalid_email");
        when(userRepository.findByEmail("invalid_email")).thenThrow(new BadCredentialsException("Usuario no encontrado"));

        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            userService.login("invalid_jwtToken");
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    /*@Test
    void testSignUp_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO userDTO = userService.signUp(user);

        assertNotNull(userDTO);
        assertEquals(user.getEmail(), userDTO.getEmail());
        verify(userRepository).save(user);
    }
*/
    /*@Test
    void testLogin_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("jwt-token");

        UserDTO userDTO = userService.login(user.getToken());

        assertEquals("jwt-token", token);
        assertNotNull(user.getLastLogin());
        verify(userRepository).save(user);
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.login(user.getToken());
        });

        assertEquals("Usuario no encontrado", thrown.getMessage());
    }*/
}