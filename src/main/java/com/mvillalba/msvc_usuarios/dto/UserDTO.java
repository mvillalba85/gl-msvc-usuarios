package com.mvillalba.msvc_usuarios.dto;

import com.mvillalba.msvc_usuarios.dto.response.Response;
import com.mvillalba.msvc_usuarios.entities.Phone;
import com.mvillalba.msvc_usuarios.entities.User;
import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO extends Response {
    private UUID id;
    private String created;
    private String lastLogin;
    private String token;
    private Boolean isActive;
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;

}
