package com.mvillalba.msvc_usuarios.services;

import com.mvillalba.msvc_usuarios.entities.User;

public interface LoginService {

    User login(String email);
}
