package com.medCenter.medCenter.service;

import com.medCenter.medCenter.dto.UserDto;
import com.medCenter.medCenter.exception.LoginException;
import com.medCenter.medCenter.model.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;
import java.util.Optional;


public interface UserService {

    UserDto userToDto(User user);

    User dtoToUser(UserDto userDto);

    List<User> findAll();

    User findById(Integer id);
    Optional<User> findByLogin(String login);

    User findByLoginForReg(String login);

    void createUser(UserDto userDto);

    void registration(UserDto userDto,Integer personalJobId, BCryptPasswordEncoder encoder) throws LoginException;


}
