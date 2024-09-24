package com.mvillalba.msvc_usuarios.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO {
    private UUID id;
    private String created;
    private String lastLogin;
    private String token;
    private Boolean isActive;

}
