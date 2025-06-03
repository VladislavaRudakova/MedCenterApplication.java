package com.medCenter.medCenter.model.repository.impl;

import com.medCenter.medCenter.dto.ClientDto;
import com.medCenter.medCenter.model.entity.Client;
import com.medCenter.medCenter.model.entity.Service;
import com.medCenter.medCenter.model.repository.ClientRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepositoryCustomImpl implements ClientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Client> findClients(ClientDto clientDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> cq = cb.createQuery(Client.class);
        Root<Client> root = cq.from(Client.class);

        List<Predicate> predicates = new ArrayList<>();
        if (clientDto.getName()!=null){
            predicates.add(cb.like(root.get("name"),"%"+ clientDto.getName()+"%"));
        }
        if (clientDto.getSurname()!=null){
            predicates.add(cb.like(root.get("surname"),"%"+ clientDto.getSurname()+"%"));
        }
        if (clientDto.getTelephoneNumber()!=null){
            predicates.add(cb.equal(root.get("telephoneNumber"),clientDto.getTelephoneNumber()));
        }
        if (clientDto.getState()!=null){
            predicates.add(cb.equal(root.get("state"),clientDto.getState()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}
