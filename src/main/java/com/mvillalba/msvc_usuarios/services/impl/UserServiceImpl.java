package com.mvillalba.msvc_usuarios.services.impl;

import com.mvillalba.msvc_usuarios.dto.ResponseDTO;
import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.mapper.util.UtilMapConverter;
import com.mvillalba.msvc_usuarios.repositories.UserRepository;
import com.mvillalba.msvc_usuarios.security.JWTUtil;
import com.mvillalba.msvc_usuarios.services.LoginService;
import com.mvillalba.msvc_usuarios.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilMapConverter utilMapConverter;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(UUID id) {
        final User user = userRepository.findById(id);
        if(user != null){
            return mapperUser(user);
        }else{
            return null;
        }
    }


    @Override
    @Transactional
    public UserDTO save(User user) {
        user.setCreated(LocalDateTime.now());
        user.setActive(Boolean.TRUE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setToken(createToken(user.getEmail(), user.getPassword()));
        final User save = userRepository.save(user);
        final UserDTO userDTO = mapperUser(save);

        return userDTO;
    }

    private String createToken(String username, String password){
        String jwt = jwtUtil.generateToken(username);
        return jwt;
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(u -> mapperUser(u)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO findByEmail(String email) {
        return userRepository.findByEmail(email).map(u -> mapperUser(u)).orElse(null);
    }

    @Override
    @Transactional
    public void updateLastLogin(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        optionalUser.ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    private UserDTO mapperUser(User user){
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setCreated(utilMapConverter.localDateTimeToString(user.getCreated()));
        userDTO.setLastLogin(user.getLastLogin() != null ? utilMapConverter.localDateTimeToString(user.getLastLogin()) : "");
//        userDTO.setLastLogin(user.getLastLogin().toString());
        userDTO.setToken(user.getToken());
        userDTO.setIsActive(user.getActive());
        return userDTO;
    }


    @Override
    @Transactional
    public User login(String token) {
        String jwt = token.replace("Bearer ", "");
        final String email = jwtUtil.extractUsername(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new BadCredentialsException ("Usuario no encontrado"));
        user.setLastLogin(LocalDateTime.now());
        User userUpdated = userRepository.save(user);
//        UserDTO userDTO = mapperUser(userUpdated);
        return userUpdated;
    }
}
