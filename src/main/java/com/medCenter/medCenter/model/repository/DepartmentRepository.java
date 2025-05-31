package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {

    Department findByName(String name);
}
