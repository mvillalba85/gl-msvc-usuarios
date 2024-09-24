package com.mvillalba.msvc_usuarios.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

    @NotBlank
    @Column(name = "NAME")
    private String name;

    @Email
    @NotBlank
    @Column(name = "EMAIL")
    private String email;

    @NotBlank
    @Column(name = "PASSWORD")
    private String password;

//    @OneToMany(cascade = CascadeType.ALL)
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

