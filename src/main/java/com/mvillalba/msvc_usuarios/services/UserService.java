package com.mvillalba.msvc_usuarios.services;

import com.mvillalba.msvc_usuarios.dto.ResponseDTO;
import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.entities.User;

import java.util.List;

public interface UserService {
    UserDTO save(User user);
    UserDTO findById(Long id);

}
