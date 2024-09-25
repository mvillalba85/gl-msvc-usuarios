package com.mvillalba.msvc_usuarios.dto.response;


import com.mvillalba.msvc_usuarios.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseRest extends ResponseRest{
    private UserDTO userDTO;

}
