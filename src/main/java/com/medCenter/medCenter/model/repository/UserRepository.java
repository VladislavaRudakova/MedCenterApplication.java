package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {

    @Query("select u from User u where u.userCredentials.login = :login")
    Optional<User> findByLogin(@Param("login") String login);

    @Query("select u from User u where u.userCredentials.login = :login")
    User findByLoginForReg(@Param("login") String login);

    @Modifying
    @Query("update User u set u.state = :state where u.id = :userId")
    void updateState(@Param("state") String state, @Param("userId") Integer userId);

}
