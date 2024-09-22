package com.mvillalba.msvc_usuarios.services.impl;

import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.mapper.util.UtilMapConverter;
import com.mvillalba.msvc_usuarios.repositories.UserRepository;
import com.mvillalba.msvc_usuarios.security.JWTUtil;
import com.mvillalba.msvc_usuarios.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilMapConverter utilMapConverter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
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
        user.setToken(createToken(user.getName(), user.getPassword()));
        final User save = userRepository.save(user);
        final UserDTO userDTO = mapperUser(save);

        return userDTO;


    }

    private String createToken(String username, String password){
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        String jwt = jwtUtil.generateToken(userDetails);
        String jwt = jwtUtil.generateToken(username);
        return jwt;
    }


    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(u -> mapperUser(u)).collect(Collectors.toList());
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



}
