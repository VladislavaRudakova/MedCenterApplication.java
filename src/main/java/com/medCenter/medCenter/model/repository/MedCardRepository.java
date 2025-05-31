package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.dto.MedCardDto;
import com.medCenter.medCenter.model.entity.MedCard;
import com.medCenter.medCenter.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface MedCardRepository extends JpaRepository<MedCard, Integer> {

    @Query("select mc from MedCard mc where mc.client.id = :clientId")
    List<MedCard> findByClientId(@Param("clientId") Integer clientId);

    @Query("select mc from MedCard mc where mc.client.id = :clientId and mc.personalJob.id = :personalJobId and mc.date = :date")
    List<MedCard> findByClientIdPersonalJobIdAndDate(@Param("clientId") Integer clientId, @Param("personalJobId")Integer personalJobId,@Param("date") Date date);

}
