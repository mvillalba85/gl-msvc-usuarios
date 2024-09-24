package com.mvillalba.msvc_usuarios.repositories;

import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.repositories.crud.UserCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private UserCrudRepository userCrudRepository;

    public User save(User user){
        return userCrudRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userCrudRepository.findByEmail(email);
    }
}
