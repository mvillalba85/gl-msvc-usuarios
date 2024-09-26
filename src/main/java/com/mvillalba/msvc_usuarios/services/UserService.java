package com.mvillalba.msvc_usuarios.services;

import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.entities.User;

public interface UserService {
    UserDTO signUp(User user);

    UserDTO login(String email);
}
