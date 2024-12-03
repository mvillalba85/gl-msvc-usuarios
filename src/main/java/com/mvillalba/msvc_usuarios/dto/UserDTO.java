package com.mvillalba.msvc_usuarios.dto;

import com.mvillalba.msvc_usuarios.dto.response.ResponseRest;
import com.mvillalba.msvc_usuarios.entities.Phone;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class UserDTO extends ResponseRest {
    private UUID id;
    private String created;
    private String lastLogin;
    private String token;
    private Boolean isActive;
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;

    public void addPhone(Phone phone){
        if(phones == null){
            phones = new ArrayList<>();
        }
        phones.add(phone);
    }

}
