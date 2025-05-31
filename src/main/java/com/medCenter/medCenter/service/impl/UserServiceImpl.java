package com.medCenter.medCenter.service.impl;

import com.medCenter.medCenter.dto.UserCredentialsDto;
import com.medCenter.medCenter.dto.UserDto;
import com.medCenter.medCenter.exception.LoginException;
import com.medCenter.medCenter.model.entity.Roles;
import com.medCenter.medCenter.model.entity.User;
import com.medCenter.medCenter.model.entity.UserCredentials;
import com.medCenter.medCenter.model.repository.UserRepository;
import com.medCenter.medCenter.service.PersonalJobService;
import com.medCenter.medCenter.service.UserCredentialsService;
import com.medCenter.medCenter.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCredentialsService userCredentialsService;
    private final PersonalJobService personalJobService;

    @Override
    public UserDto userToDto(User user) {
        UserCredentialsDto userCredentialsDto = userCredentialsService.userCredToDto(user.getUserCredentials());
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userCredentials(userCredentialsDto)
                .role(user.getRole())
                .email(user.getEmail())
                .build();
        if (user.getState() != null) {
            userDto.setState(user.getState());
        }
        return userDto;
    }

    @Override
    public User dtoToUser(UserDto userDto) {
        UserCredentials userCredentials = userCredentialsService.dtoToUserCred(userDto.getUserCredentials());
        User user = User.builder()
                .username(userDto.getUsername())
                .userCredentials(userCredentials)
                .role(userDto.getRole())
                .email(userDto.getEmail())
                .build();
        if (userDto.getState() != null) {
            user.setState(userDto.getState());
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public User findByLoginForReg(String login) {
        return userRepository.findByLoginForReg(login);
    }

    @Transactional
    @Override
    public void createUser(UserDto userDto) {
        User user = dtoToUser(userDto);
        userRepository.save(user);
    }


    @Transactional
    @Override
    public void registration(UserDto userDto, Integer personalJobId, BCryptPasswordEncoder encoder) throws LoginException {

        User user = findByLoginForReg(userDto.getUserCredentials().getLogin());
        if (user != null) {
            throw new LoginException();

        } else {
            UserCredentials userCredentials = UserCredentials.builder()
                    .login(userDto.getUserCredentials().getLogin())
                    .password(encoder.encode(userDto.getUserCredentials().getPassword())).build();

            UserCredentialsDto userCredentialsDto = userCredentialsService.userCredToDto(userCredentials);

            userDto.setUserCredentials(userCredentialsDto);

            if (userDto.getRole() == null) {
                userDto.setRole(Roles.ROLE_CLIENT.toString());
            }

            createUser(userDto);

            if (personalJobId != null) {
                User userSaved = userRepository.findByLoginForReg(userDto.getUserCredentials().getLogin());
                personalJobService.updateUserId(userSaved.getId(), personalJobId);
            }
        }
    }


}
