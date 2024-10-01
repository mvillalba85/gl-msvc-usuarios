package com.mvillalba.msvc_usuarios.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "ID")
    private UUID id;

    @Column(name = "NAME")
    private String name;

    @NotEmpty(message = "El correo electrónico no puede estar vacío")
    @Email(message = "Correo electrónico inválido")
    @Column(name = "EMAIL", unique = true)
    private String email;

    @NotEmpty(message = "La contraseña no puede estar vacío")
    @Column(name = "PASSWORD")
    private String password;

    @ElementCollection
    private List<Phone> phones;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Column(name = "LAST_LOGIN")
    private LocalDateTime lastLogin;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "ACTIVE")
    private Boolean active;

    public void addPhone(Phone phone){
        if(phones == null){
            phones = new ArrayList<>();
        }
        phones.add(phone);
    }

}

