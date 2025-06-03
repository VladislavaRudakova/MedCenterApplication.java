package com.medCenter.medCenter.model.repository.impl;

import com.medCenter.medCenter.dto.TicketDto;
import com.medCenter.medCenter.model.entity.Service;
import com.medCenter.medCenter.model.entity.Ticket;
import com.medCenter.medCenter.model.repository.TicketRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketRepositoryCustomImpl implements TicketRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Ticket> findTickets(TicketDto ticketDto) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ticket> cq = cb.createQuery(Ticket.class);
        Root<Ticket> root = cq.from(Ticket.class);
        Join<Ticket, Service> serviceJoin = root.join("service");
        List<Predicate> predicates = new ArrayList<>();
        if (ticketDto.getDate() != null) {
            predicates.add(cb.equal(root.get("date"), ticketDto.getDate()));
        }
        if (ticketDto.getService() != null) {
            predicates.add(cb.equal(serviceJoin.get("type"), ticketDto.getService().getType()));
        }
        if (ticketDto.getState() != null) {
            predicates.add(cb.equal(root.get("state"), ticketDto.getState()));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}
