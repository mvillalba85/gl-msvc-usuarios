package com.mvillalba.msvc_usuarios.repositories;

import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.repositories.crud.UserCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    @Autowired
    private UserCrudRepository userCrudRepository;

    public User findById(UUID id) {
        return userCrudRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return (List<User>) userCrudRepository.findAll();
    }

    public User save(User user){
        user.setId(UUID.randomUUID());
        return userCrudRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userCrudRepository.findByEmail(email);
    }
}
