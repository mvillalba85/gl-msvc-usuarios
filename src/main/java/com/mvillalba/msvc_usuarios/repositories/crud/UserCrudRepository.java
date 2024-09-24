package com.mvillalba.msvc_usuarios.repositories.crud;

import com.mvillalba.msvc_usuarios.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCrudRepository extends CrudRepository<User, UUID> {

    Optional<User> findByEmail(String email);
}
