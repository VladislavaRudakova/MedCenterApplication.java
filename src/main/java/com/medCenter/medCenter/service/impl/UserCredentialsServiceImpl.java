package com.medCenter.medCenter.service.impl;

import com.medCenter.medCenter.dto.UserCredentialsDto;
import com.medCenter.medCenter.model.entity.UserCredentials;
import com.medCenter.medCenter.service.UserCredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCredentialsServiceImpl implements UserCredentialsService {


    @Override
    public UserCredentialsDto userCredToDto(UserCredentials userCredentials) {

        UserCredentialsDto userCredentialsDto = UserCredentialsDto.builder()
                .login(userCredentials.getLogin())
                .password(userCredentials.getPassword()).build();
        if (userCredentials.getState() != null) {
            userCredentialsDto.setState(userCredentials.getState());
        }
        return userCredentialsDto;
    }

    @Override
    public UserCredentials dtoToUserCred(UserCredentialsDto userCredentialsDto) {
        UserCredentials userCredentials = UserCredentials.builder()
                .login(userCredentialsDto.getLogin())
                .password(userCredentialsDto.getPassword()).build();
        if (userCredentialsDto.getState() != null) {
            userCredentials.setState(userCredentialsDto.getState());
        }
        return userCredentials;
    }
}
