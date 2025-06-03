package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.Service;
import com.medCenter.medCenter.model.entity.User;

import java.util.List;

public interface UserRepositoryCustom {

    User findUser (String login);

}
