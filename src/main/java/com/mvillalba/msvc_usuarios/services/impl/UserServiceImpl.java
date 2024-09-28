package com.mvillalba.msvc_usuarios.services.impl;

import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.exceptions.UserException;
import com.mvillalba.msvc_usuarios.mapper.util.UtilMapConverter;
import com.mvillalba.msvc_usuarios.repositories.UserRepository;
import com.mvillalba.msvc_usuarios.security.JWTUtil;
import com.mvillalba.msvc_usuarios.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilMapConverter utilMapConverter;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public UserDTO signUp(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserException("Ya existe un usuario con el mismo email");
        }
        if(!isValid(user.getPassword())){
            throw new UserException("La contraseña debe tener al menos una mayúscula y dos números.");
        }

        buildUser(user);

        final User save = userRepository.save(user);
        final UserDTO userDTO = mapperUser(save);

        return userDTO;
    }

    private void buildUser(User user) {
        user.setCreated(LocalDateTime.now());
        user.setActive(Boolean.TRUE);
        user.setPassword(encryptPassword(user));
//        user.setToken(createToken(user.getEmail(), user.getPassword()));
        String tokenJwt = jwtUtil.generateToken(user.getEmail());
        user.setToken(tokenJwt);
        user.setToken(createToken(user.getEmail()));
    }

    private String encryptPassword(User user) {
        return passwordEncoder.encode(user.getPassword());
    }

    public static boolean isValid(String password) {
        String regexp = "^(?=.*[A-Z])(?=(.*\\d){2}).{8,12}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private String createToken(String username, String password){
        String jwt = jwtUtil.generateToken(username);
        return jwt;
    }

    private String createToken(String email){
        String jwt = jwtUtil.generateToken(email);
        return jwt;
    }

    @Override
    @Transactional
    public UserDTO login(String token) {
        String jwt = token.replace("Bearer ", "");
        final String email = jwtUtil.extractUsername(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new BadCredentialsException ("Usuario no encontrado"));

        if(!Boolean.TRUE.equals(user.getActive())){
            throw new UserException("Usuario no activo");
        }
        user.setLastLogin(LocalDateTime.now());
        User userUpdated = userRepository.save(user);
        if(userUpdated == null){
            throw new UserException("Error al intentar guardar el usuario");
        }
        return mapperUser(userUpdated);
    }

    private UserDTO mapperUser(User user){
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setCreated(user.getCreated() != null? utilMapConverter.localDateTimeToString(user.getCreated()):"");
        userDTO.setLastLogin(user.getLastLogin() != null ? utilMapConverter.localDateTimeToString(user.getLastLogin()) : "");
        userDTO.setToken(user.getToken());
        userDTO.setIsActive(user.getActive());
        userDTO.setPhones(user.getPhones());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró el usuario con el email: " + email));

        return  new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>());

    }
}
