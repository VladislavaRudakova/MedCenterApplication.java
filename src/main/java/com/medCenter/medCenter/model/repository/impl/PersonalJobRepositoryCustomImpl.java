package com.medCenter.medCenter.model.repository.impl;

import com.medCenter.medCenter.dto.PersonalJobWithoutPersonalDto;
import com.medCenter.medCenter.model.entity.Department;
import com.medCenter.medCenter.model.entity.Personal;
import com.medCenter.medCenter.model.entity.PersonalJob;
import com.medCenter.medCenter.model.repository.PersonalJobRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PersonalJobRepositoryCustomImpl implements PersonalJobRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PersonalJob> findPersonalJob(PersonalJobWithoutPersonalDto personal) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PersonalJob> cq = cb.createQuery(PersonalJob.class);
        Root<PersonalJob> root = cq.from(PersonalJob.class);

        Join<PersonalJob, Personal> personalJoin = root.join("personal");
        Join<PersonalJob, Department> departmentJoin = root.join("department");

        List<Predicate> predicates = new ArrayList<>();
        if (personal.getName() != null && !personal.getName().isEmpty()) {
            predicates.add(cb.like(personalJoin.get("name"), "%" + personal.getName() + "%"));
        }
        if (personal.getSurname() != null && !personal.getSurname().isEmpty()) {
            predicates.add(cb.like(personalJoin.get("surname"), "%" + personal.getSurname() + "%"));
        }
        if (personal.getBirthdate() != null) {
            predicates.add(cb.equal(personalJoin.get("dob"), personal.getBirthdate()));
        }
        if (personal.getEmploymentDate() != null) {
            predicates.add(cb.equal(personalJoin.get("employmentDate"), personal.getEmploymentDate()));
        }
        if (personal.getDismissalDate() != null) {
            predicates.add(cb.equal(personalJoin.get("dismissalDate"), personal.getDismissalDate()));
        }
        if (personal.getExperience() != null) {
            predicates.add(cb.equal(personalJoin.get("experience"), personal.getExperience()));
        }
        if (personal.getJobTitle() != null && !personal.getJobTitle().isEmpty()) {
            predicates.add(cb.like(root.get("jobTitle"), personal.getJobTitle()));
        }
        if (personal.getDepartmentName() != null && !personal.getDepartmentName().isEmpty()) {
            predicates.add(cb.like(departmentJoin.get("name"), personal.getDepartmentName()));
        }
        if (personal.getState() != null) {
            predicates.add(cb.equal(root.get("state"), personal.getState()));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}
