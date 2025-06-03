package com.medCenter.medCenter.model.repository;

import com.medCenter.medCenter.model.entity.Service;
import org.springframework.context.annotation.Lazy;

import java.util.List;

public interface ServiceRepositoryCustom {

    List<Service> findService (String type, Double price);

}
