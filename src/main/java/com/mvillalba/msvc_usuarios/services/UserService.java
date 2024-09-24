package com.mvillalba.msvc_usuarios.services;

import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO save(User user);
    UserDTO findById(UUID id);
    List<UserDTO> findAll();

    UserDTO findByEmail(String token);

    void updateLastLogin(String email);

}
