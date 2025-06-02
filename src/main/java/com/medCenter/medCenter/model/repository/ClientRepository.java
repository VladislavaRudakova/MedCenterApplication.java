package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.Client;
import com.medCenter.medCenter.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

public interface  ClientRepository extends JpaRepository<Client,Integer> {

    Client findByUserId(Integer userId);

    @Query("select c from Client c where c.name = :name and c.surname = :surname")
    List<Client> findByNameAndSurname(@Param("name") String name, @Param("surname") String surname);

    @Modifying(clearAutomatically = true)
    @Query("update Client c set c.state = :state where c.id = :clientId")
    void updateState(@Param("state") String state, @Param("clientId") Integer clientId);

    @Modifying(clearAutomatically = true)
    @Query("update Client c set c.user.id = :userId where c.id = :clientId")
    void updateUserId(@Param("userId") Integer userId, @Param("clientId") Integer clientId);

}
