package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.Client;
import com.medCenter.medCenter.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

public interface  ClientRepository extends JpaRepository<Client,Integer>, ClientRepositoryCustom {

    Client findByUserId(Integer userId);

    @Query("select c from Client c where c.name = :name and c.surname = :surname")
    List<Client> findByNameAndSurname(@Param("name") String name, @Param("surname") String surname);

    @Modifying(clearAutomatically = true)
    @Query("update Client c set c.state = :state where c.id = :clientId")
    void updateState(@Param("state") String state, @Param("clientId") Integer clientId);

    @Modifying(clearAutomatically = true)
    @Query("update Client c set c.user.id = :userId where c.id = :clientId")
    void updateUserId(@Param("userId") Integer userId, @Param("clientId") Integer clientId);

    @Modifying
    @Query("""
        update Client c set
           c.name = case when :name is not null and :name!='' then :name else c.name end,
           c.surname = case when :surname is not null and :surname!='' then :surname else c.surname end,
           c.telephoneNumber = case when :telephoneNumber is not null then :telephoneNumber else c.telephoneNumber end,
           c.state = case when :state is not null then :state else c.state end
           where c.id = :id""")
    void updateClient(@Param("name") String name, @Param("surname") String surname, @Param("telephoneNumber") String telephoneNumber,
                      @Param("state") String state, @Param("id") Integer clientId );



}
