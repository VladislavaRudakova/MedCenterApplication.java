package com.medCenter.medCenter;

import com.medCenter.medCenter.dto.ServiceDto;
import com.medCenter.medCenter.service.impl.ServiceServiceImpl;

//import org.junit.Assert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


//@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ServiceServiceTest {

    @Autowired
    private ServiceServiceImpl serviceService;

    @Test
    public void createServiceTest() {


        ServiceDto serviceDto = ServiceDto.builder()
                .type("testService")
                .price(100.0).build();

        serviceService.createService(serviceDto);

        ServiceDto service = serviceService.findByType("testService");
        System.out.println("SERVICE!!!!!!" + service);
        Assertions.assertNotNull(service);

    }


}
