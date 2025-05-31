package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Integer> {

}
