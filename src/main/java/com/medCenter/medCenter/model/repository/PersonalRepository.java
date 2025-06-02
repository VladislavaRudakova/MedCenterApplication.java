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

    @Modifying
    @Query("""
        update Personal p set
           p.name = case when :name is not null then :name else p.name end,
           p.surname = case when :surname is not null then :surname else p.surname end
           p.birthDate = case when :birthDate is not null then :birthDate else p.birthDate end
           p.employmentDate = case when :employmentDate is not null then :employmentDate else p.employmentDate end
           p.experience = case when :experience is not null then :experience else p.experience end
         
           where p.id = :id""")
    void updatePersonalJob(@Param("name") String name, @Param("surname") String surname, @Param("birthDate") Date birthDate,
                           @Param("experience") Integer experience, @Param("employmentDate") Date employmentDate);




}
