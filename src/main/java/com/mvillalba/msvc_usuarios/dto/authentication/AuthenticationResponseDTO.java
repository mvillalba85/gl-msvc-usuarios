package com.mvillalba.msvc_usuarios.dto.authentication;

public class AuthenticationResponseDTO {
    private String jwt;

    public AuthenticationResponseDTO(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
