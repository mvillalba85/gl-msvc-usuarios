package com.mvillalba.msvc_usuarios.services.impl;

import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.exceptions.UserException;
import com.mvillalba.msvc_usuarios.mapper.util.UtilMapConverter;
import com.mvillalba.msvc_usuarios.repositories.UserRepository;
import com.mvillalba.msvc_usuarios.security.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UtilMapConverter utilMapConverter;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("Password123");
        user.setActive(true);

    }

    @Test
    void testSignUpSuccess() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("coded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(utilMapConverter.localDateTimeToString(any(LocalDateTime.class))).thenReturn("2023-09-27T00:00:00");

        UserDTO userDTO = userService.signUp(user);

        assertNotNull(userDTO);
        assertEquals(user.getEmail(), userDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));


    }

    @Test
    public void testSignUpUserAlreadyExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserException exception = assertThrows(UserException.class, () -> userService.signUp(user));

        assertEquals("Ya existe un usuario con el mismo email", exception.getMessage());
    }

    @Test
    public void testSignUpInvalidPassword() {
        user.setPassword("invalid"); // Sin mayúscula y menos de dos números

        UserException exception = assertThrows(UserException.class, () -> userService.signUp(user));
        assertEquals("La contraseña debe tener al menos una mayúscula y dos números.", exception.getMessage());
    }

    @Test
    public void testLoginSuccess() {
        String token = "Bearer jwtToken";
        when(jwtUtil.extractUsername("jwtToken")).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(utilMapConverter.localDateTimeToString(any(LocalDateTime.class))).thenReturn("2023-09-27T00:00:00");
        when(userRepository.save(user)).thenReturn(user);

        UserDTO userDTO = userService.login(token);

        assertNotNull(userDTO);
        assertEquals(user.getEmail(), userDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testLoginUserNotUpdated() {
        String token = "Bearer jwtToken";
        when(jwtUtil.extractUsername("jwtToken")).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        when(utilMapConverter.localDateTimeToString(any(LocalDateTime.class))).thenReturn("2023-09-27T00:00:00");

        UserException exception = assertThrows(UserException.class, () -> userService.login(token));
        assertEquals("Error al intentar guardar el usuario", exception.getMessage());

    }


    @Test
    public void testLoginUserNotFound() {
        String token = "Bearer jwtToken";
        when(jwtUtil.extractUsername("jwtToken")).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> userService.login(token));
        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    public void testLoginUserNotActive() {
        user.setActive(false);
        String token = "Bearer jwtToken";
        when(jwtUtil.extractUsername("jwtToken")).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserException exception = assertThrows(UserException.class, () -> userService.login(token));
        assertEquals("Usuario no activo", exception.getMessage());
    }


    @Test
    void loadUserByUsernameSuccess() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        assertNotNull(userDetails);
    }

    @Test
    void loadUserByUsernameNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername(user.getEmail()));
        assertEquals("No se encontró el usuario con el email: " + user.getEmail(), exception.getMessage());
    }
}