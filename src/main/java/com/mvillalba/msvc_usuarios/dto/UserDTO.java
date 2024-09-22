package com.mvillalba.msvc_usuarios.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO {
    private Long id;
    private String created;
    private String lastLogin;
    private String token;
    private Boolean isActive;

}
