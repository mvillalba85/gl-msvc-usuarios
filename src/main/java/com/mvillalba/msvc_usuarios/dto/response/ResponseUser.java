package com.mvillalba.msvc_usuarios.dto.response;

import com.mvillalba.msvc_usuarios.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseUser extends Response{

    private UserDTO userDTO;
}
