package com.mvillalba.msvc_usuarios.repositories.crud;

import com.mvillalba.msvc_usuarios.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserCrudRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
