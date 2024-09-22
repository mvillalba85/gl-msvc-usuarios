package com.mvillalba.msvc_usuarios.services.impl;

import com.mvillalba.msvc_usuarios.dto.UserDTO;
import com.mvillalba.msvc_usuarios.entities.User;
import com.mvillalba.msvc_usuarios.mapper.util.UtilMapConverter;
import com.mvillalba.msvc_usuarios.repositories.UserRepository;
import com.mvillalba.msvc_usuarios.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UtilMapConverter utilMapConverter;

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        final User user = userRepository.findById(id);
        if(user != null){
            return mapperUser(user);
        }else{
            return null;
        }
    }


    @Override
    @Transactional
    public UserDTO save(User user) {
        user.setCreated(LocalDateTime.now());
        user.setActive(Boolean.TRUE);

        final User save = userRepository.save(user);
        final UserDTO userDTO = mapperUser(save);

        return userDTO;


    }

    private UserDTO mapperUser(User user){
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setCreated(utilMapConverter.localDateTimeToString(user.getCreated()));
        userDTO.setLastLogin(user.getLastLogin() != null ? utilMapConverter.localDateTimeToString(user.getLastLogin()) : "");
//        userDTO.setLastLogin(user.getLastLogin().toString());
        userDTO.setToken(user.getToken());
        userDTO.setIsActive(user.getActive());
        return userDTO;
    }


}
