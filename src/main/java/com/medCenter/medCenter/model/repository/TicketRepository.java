package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket, Integer>, TicketRepositoryCustom {

    @Query("select t.personalJob from Ticket t where t.service.type = :serviceType and t.date = :date and t.time >= :minTime and t.time <= :maxTime and t.state= :state")
    List<PersonalJob> findByServiceDateTime(@Param("serviceType") String serviceType, @Param("date") Date date, @Param("minTime") Time minTime, @Param("maxTime") Time maxTime, @Param("state") String state);

    @Query("select t from Ticket t where t.personalJob.id = :doctorId and t.time = :time")
    Ticket findByDoctorIdAndTime(@Param("doctorId") Integer doctorId, @Param("time") Time time);

    @Query("select t from Ticket t where t.personalJob.id = :personalJobId and t.date = :date")
    List<Ticket> findByPersonalJobAndDate(@Param("personalJobId") Integer personalJobId, @Param("date") Date date);

    @Query("select t from Ticket t where t.personalJob.id = :personalJobId and t.date >= :date1 and t.date <= :date2")
    List<Ticket> findByPersonalJobAndDatePeriod(@Param("personalJobId") Integer personalJobId, @Param("date1") Date date1, @Param("date2") Date date2);

    @Query("select t from Ticket t where t.service.id = :serviceId and t.date = :date")
    List<Ticket> findByServiceIdAndDate(@Param("serviceId") Integer serviceId, @Param("date") Date date);

    List<Ticket> findByClientId(Integer clientId);

    @Query("select t from Ticket t where t.personalJob.id = :personalJobId")
    List<Ticket> findByPersonalJob(@Param("personalJobId") Integer personalJob);

    @Query("select t from Ticket t where t.personalJob.id is null and t.client.id is not null")
    List<Ticket> findWherePersonalIsNull();

    @Query("select t from Ticket t where t.personalJob.id = :personalJobId and t.client.id is not null and t.state= :state")
    List<Ticket> findActualByPersonalJob(@Param("personalJobId") Integer personalJobId, @Param("state") String state);

    @Query("select t from Ticket t where t.client.id = :clientId and state = :state")
    List<Ticket> findActualByClient(@Param("clientId") Integer clientId, @Param("state") String state);

    @Query("select date from Ticket t where t.personalJob.id = :personalJobId")
    Set<String> findDates(@Param("personalJobId") Integer personalJobId);

    @Modifying
    @Query("update Ticket t set t.client.id = :clientId, t.state = :state, t.cancelRequestFromRole = null where t.id = :ticketId")
    void updateClientAndState(@Param("clientId") Integer clientId, @Param("state") String state, @Param("ticketId") Integer ticketId);


    @Modifying
    @Query("update Ticket t set t.state = :state where t.id = :ticketId")
    void updateState(@Param("state") String state, @Param("ticketId") Integer ticketId);


    @Modifying
    @Query("update Ticket t set t.state = :state where t.date < :date")
    void updateStateByDate(@Param("state") String state, @Param("date") Date date);


    @Modifying
    @Query("update Ticket t set t.subState = :subState where t.id = :ticketId")
    void updateSubState(@Param("subState") String subState, @Param("ticketId") Integer ticketId);

    @Modifying
    @Query("update Ticket t set t.cancelRequestFromRole = :role where t.id = :ticketId")
    void updateCancelFromRole(@Param("role") String role, @Param("ticketId") Integer ticketId);


    @Modifying
    @Query("update Ticket t set t.subState = :subState, t.cancelRequestFromRole = :role where t.id = :ticketId")
    void updateSubStateAndRole(@Param("subState") String subState, @Param("role") String role, @Param("ticketId") Integer ticketId);

    @Modifying
    @Query("update Ticket t set t.client.id = null, t.state = :state, t.subState = :subState where t.id = :ticketId")
    void updateClientToNullStateAndSubState(@Param("state") String state, @Param("subState") String subState, @Param("ticketId") Integer ticketId);

    List<Ticket> findByState(String state);

    List<Ticket> findBySubState(String state);

}
