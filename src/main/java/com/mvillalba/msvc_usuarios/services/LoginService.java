package com.mvillalba.msvc_usuarios.services;

import com.mvillalba.msvc_usuarios.dto.UserDTO;

public interface LoginService {

    UserDTO login(String email);
}
