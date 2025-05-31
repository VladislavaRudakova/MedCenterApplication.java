package com.medCenter.medCenter.service;

import com.medCenter.medCenter.dto.UserCredentialsDto;
import com.medCenter.medCenter.model.entity.UserCredentials;

public interface UserCredentialsService {

    UserCredentialsDto userCredToDto(UserCredentials userCredentials);

    UserCredentials dtoToUserCred(UserCredentialsDto userCredentialsDto);




}
