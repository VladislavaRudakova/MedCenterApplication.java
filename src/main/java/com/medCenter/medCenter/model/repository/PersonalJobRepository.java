package com.medCenter.medCenter.model.repository;


import com.medCenter.medCenter.model.entity.PersonalJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PersonalJobRepository extends JpaRepository<PersonalJob, Integer>, PersonalJobRepositoryCustom {

    @Query("select p from PersonalJob p where p.jobTitle = :job")
    List<PersonalJob> findByJob(@Param("job") String job);


    @Query("select p from PersonalJob p where p.user.id = :userId")
    PersonalJob findByUserId(@Param("userId") Integer userId);

    @Query("select p from PersonalJob p where p.personal.name= :name and p.personal.surname = :surname and p.jobTitle = :job")
    List<PersonalJob> findByNameSurnameJob( @Param("name") String name, @Param("surname") String surname, @Param("job") String job);

    @Query("select jobTitle from PersonalJob")
    Set<String> findAllJobs();

    @Query("select jobTitle from PersonalJob p where p.jobTitle LIKE CONCAT(:job, '%')")
    Set<String> findJobsNearly(@Param("job") String job);

    @Modifying(clearAutomatically = true)
    @Query("update PersonalJob p set p.user.id = :userId where p.id = :personalJobId")
    void updateUserId(@Param("userId") Integer userId, @Param("personalJobId") Integer personalJobId);

    @Modifying
    @Query("""
        update PersonalJob p SET
           p.jobTitle = case when :jobTitle IS NOT NULL THEN :jobTitle ELSE p.jobTitle END,
           p.department.id = CASE WHEN :departmentId IS NOT NULL THEN :departmentId ELSE p.department.id END
           WHERE p.id = :id""")
    void updatePersonalJob(@Param("jobTitle") String jobTitle, @Param("departmentId") Integer departmentId, @Param("departmentId") Integer personalJobId);


    @Modifying(clearAutomatically = true)
    @Query("update PersonalJob p set p.state = :state where p.personal.id = :personalId")
    void updateState(@Param("state") String state, @Param("personalId") Integer personalId);



}
