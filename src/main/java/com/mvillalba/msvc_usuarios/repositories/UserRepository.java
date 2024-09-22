package com.mvillalba.msvc_usuarios.repositories;

import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.repositories.crud.UserCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private UserCrudRepository userCrudRepository;

    public User findById(Long id) {
        return userCrudRepository.findById(id).orElse(null);
    }

    public User save(User user){
        return userCrudRepository.save(user);
    }
}
