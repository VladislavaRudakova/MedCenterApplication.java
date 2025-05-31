package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query("""
            select sc from Schedule sc
            where sc.personalJob.user.id = :userId and sc.date = :date""")
    List<Schedule> findByDateAndUserId(@Param("userId") Integer userId, @Param("date") Date date);

    @Query("""
            select sc from Schedule sc
            where sc.personalJob.id = :personalJobId and sc.date = :date""")
    List<Schedule> findByDateAndPersonalId(@Param("personalJobId") Integer personalJobId, @Param("date") Date date);


    @Query("""
            select sc from Schedule sc
            where sc.service.id = :serviceId and sc.date = :date""")
    List<Schedule> findByDateAndServiceId(@Param("serviceId") Integer serviceId, @Param("date") Date date);


    @Query("""
            select sc from Schedule sc
            where sc.personalJob.user.id = :userId and sc.state=:state
            order by sc.date asc""")
    List<Schedule> findByUserId(@Param("userId") Integer userId, @Param("state") String state);


    @Query("""
            select sc from Schedule sc
            where sc.personalJob.id = :personalJobId and sc.state=:state
            order by sc.date asc""")
    List<Schedule> findByPersonalId(@Param("personalJobId") Integer personalJobId, @Param("state") String state);


    @Query("""
            select sc from Schedule sc
            where sc.service.id = :serviceId and sc.state=:state
            order by sc.date asc""")
    List<Schedule> findByServiceIdAndState(@Param("serviceId") Integer serviceId, @Param("state") String state);


    @Query("""
            select sc from Schedule sc
            where sc.personalJob.id = :personalJobId
            and sc.date >= :minDate
            and sc.date <= :maxDate
            order by sc.date asc""")
    List<Schedule> findByDatePeriodAndPersonalId(@Param("minDate") Date minDate, @Param("maxDate") Date maxDate, @Param("personalJobId") Integer personalJobId);


    @Query("""
            select sc from Schedule sc
            where sc.service.id = :serviceId
            and sc.date >= :minDate
            and sc.date <= :maxDate
            order by sc.date asc""")
    List<Schedule> findByDatePeriodAndServiceId(@Param("minDate") Date minDate, @Param("maxDate") Date maxDate, @Param("serviceId") Integer serviceId);


    @Query("""
            select sc from Schedule sc
            where sc.personalJob.id = :personalJobId
            and sc.date >= :minDate
            and sc.date <= :maxDate
            and sc.dayOff is null
            order by sc.date asc""")
    List<Schedule> findByDatePeriodAndPersonalIdNoDayOff(@Param("minDate") Date minDate, @Param("maxDate") Date maxDate, @Param("personalJobId") Integer personalJobId);


    @Query("""
            select sc from Schedule sc
            where sc.service.id = :serviceId
            and sc.date >= :minDate
            and sc.date <= :maxDate
            and sc.dayOff is null
            order by sc.date asc""")
    List<Schedule> findByDatePeriodAndServiceIdNoDayOff(@Param("minDate") Date minDate, @Param("maxDate") Date maxDate, @Param("serviceId") Integer serviceId);


    @Modifying(clearAutomatically = true)
    @Query("update Schedule sc set sc.dayOff = :dayOff, sc.startTime = null, sc.endTime = null where sc.id = :scheduleId")
    void updateDayOff(@Param("dayOff") String dayOff, @Param("scheduleId") Integer scheduleId);


    @Modifying(clearAutomatically = true)
    @Query("update Schedule sc set sc.startTime = :startTime, sc.endTime = :endTime, sc.dayOff = null where sc.id = :scheduleId")
    void updateTime(@Param("startTime") Time startTime, @Param("endTime") Time endTime, @Param("scheduleId") Integer scheduleId);


    @Modifying(clearAutomatically = true)
    @Query("update Schedule sc set sc.startTime = :startTime, sc.dayOff = null where sc.id = :scheduleId")
    void updateStartTime(@Param("startTime") Time startTime, @Param("scheduleId") Integer scheduleId);


    @Modifying(clearAutomatically = true)
    @Query("update Schedule sc set sc.endTime = :endTime, sc.dayOff = null where sc.id = :scheduleId")
    void updateEndTime(@Param("endTime") Time endTime, @Param("scheduleId") Integer scheduleId);


    @Modifying(clearAutomatically = true)
    @Query("update Schedule sc set sc.state = :state where sc.id = :scheduleId")
    void updateState(@Param("state") String state, @Param("scheduleId") Integer scheduleId);


    @Modifying(clearAutomatically = true)
    @Query("update Schedule sc set sc.state = :state where sc.date < :date")
    void updateStateByDate(@Param("state") String state, @Param("date") Date date);


    @Modifying(clearAutomatically = true)
    @Query("""
           update Schedule sc SET
           sc.startTime = :startTime,
           sc.endTime =:endTime, sc.dayOff=null
           WHERE sc.id = :scheduleId""")
    void updateSchedule(@Param("startTime")  Time startTime, @Param("endTime")  Time endTime, @Param("scheduleId") Integer scheduleId);




}
