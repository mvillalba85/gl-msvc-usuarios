package com.mvillalba.msvc_usuarios.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Column(name = "NAME")
    private String name;

    @Email(message = "El correo electrónico no es válido")
    @NotEmpty(message = "El correo electrónico no puede estar vacío")
    @Column(name = "EMAIL", unique = true)
    private String email;

    @NotEmpty(message = "La contraseña no puede estar vacío")
    @Pattern(regexp = "^(?=.*[A-Z])(?=(.*\\d){2}).{8,12}$", message = "La contraseña debe tener al menos una mayúscula y dos números.")
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

    // Getters and Setters
}

