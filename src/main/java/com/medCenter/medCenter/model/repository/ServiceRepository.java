package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer>, ServiceRepositoryCustom {

    @Query("select type from Service")
    Set<String> findAllTypes();

    Service findByType(String type);

    @Query("select s from Service s where s.type like :type")
    List<Service> findByTypeLike(@RequestParam String type);

    @Modifying(clearAutomatically = true)
    @Query("""
        update Service s set
           s.type = case when :type is not null and :type != '' then :type else s.type end,
           s.price = case when :price is not null then :price else s.price end,
           s.state = case when :state is not null then :state else s.state end
           where s.id = :id""")
    void updateService(@Param("type") String type, @Param("price") Double price, @Param("state") String state,
                           @Param("id") Integer serviceId );




}
