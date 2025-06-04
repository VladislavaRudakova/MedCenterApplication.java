package com.medCenter.medCenter.model.repository;


import com.medCenter.medCenter.model.entity.PersonalJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
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

    @Query("select jobTitle from PersonalJob p where p.jobTitle like concat(:job, '%')")
    Set<String> findJobsNearly(@Param("job") String job);

    @Modifying(clearAutomatically = true)
    @Query("update PersonalJob p set p.user.id = :userId where p.id = :personalJobId")
    void updateUserId(@Param("userId") Integer userId, @Param("personalJobId") Integer personalJobId);

    @Modifying
    @Query("""
        update PersonalJob p set
           p.jobTitle = case when :jobTitle is not null then :jobTitle else p.jobTitle end,
           p.department.id = case when :departmentId is not null then :departmentId else p.department.id end
           where p.id = :id""")
    void updatePersonalJob1(@Param("jobTitle") String jobTitle, @Param("departmentId") Integer departmentId, @Param("personalJobId") Integer personalJobId);

    @Modifying(clearAutomatically = true)
    @Query("""
        update PersonalJob p set
           p.jobTitle = case when :jobTitle is not null then :jobTitle else p.jobTitle end,
           p.department.id = case when :departmentId is not null then :departmentId else p.department.id end,
           p.state = case when :state is not null then :state else p.state end
           where p.id = :id""")
    void updatePersonalJob(@Param("jobTitle") String jobTitle, @Param("departmentId") Integer departmentId, @Param("state") String state,
                           @Param("id") Integer personalJobId );


    @Modifying(clearAutomatically = true)
    @Query("update PersonalJob p set p.personal.dismissalDate = :dismissalDate where p.personal.id = :personalId")
    void updateDismissalDate(@Param("dismissalDate") Date dismissalDate, @Param("personalId") Integer personalJobId);



    @Modifying(clearAutomatically = true)
    @Query("update PersonalJob p set p.state = :state where p.personal.id = :personalId")
    void updateState(@Param("state") String state, @Param("personalId") Integer personalId);



}
