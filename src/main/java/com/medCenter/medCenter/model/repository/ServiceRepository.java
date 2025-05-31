package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer>,ServiceRepositoryCustom{

    @Query("select type from Service")
    Set<String> findAllTypes();

    Service findByType(String type);

    @Query("select s from Service s where s.type like :type")
    List<Service> findByTypeLike(@RequestParam String type);


}
