package com.mvillalba.msvc_usuarios.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

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

