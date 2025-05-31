package com.medCenter.medCenter.model.repository.impl;

import com.medCenter.medCenter.model.entity.Service;
import com.medCenter.medCenter.model.repository.ServiceRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class ServiceRepositoryCustomImpl implements ServiceRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Service> findService(String type, Double price) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Service> cq = cb.createQuery(Service.class);
        Root<Service> root = cq.from(Service.class);

        List<Predicate> predicates = new ArrayList<>();
        if (type!=null){
            predicates.add(cb.like(root.get("type"),"%"+type+"%"));
        }
        if (price!=null){
            predicates.add(cb.equal(root.get("price"),price));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}
