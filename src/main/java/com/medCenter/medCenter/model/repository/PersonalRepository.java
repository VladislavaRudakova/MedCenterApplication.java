package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.Personal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface PersonalRepository extends JpaRepository<Personal, Integer> {

    List<Personal> findByNameAndSurname(String name, String surname);

    @Query("select p from Personal p where p.name = :name and p.surname = :surname and p.birthDate = :birthDate")
    List<Personal> findByNameSurnameAndBirthDate(@Param("name") String name, @Param("surname") String surname, @Param("birthDate") Date birthDate);

    @Modifying
    @Query("update Personal p set p.state = :state where p.id = :personalId")
    void updateState(@Param("state") String state, @Param("personalId") Integer personalId);


}
