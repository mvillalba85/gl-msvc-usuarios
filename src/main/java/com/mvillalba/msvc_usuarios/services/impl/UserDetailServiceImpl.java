package com.mvillalba.msvc_usuarios.services.impl;

import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró el usuario con el email: " + email));

        return  new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>());

    }
}
