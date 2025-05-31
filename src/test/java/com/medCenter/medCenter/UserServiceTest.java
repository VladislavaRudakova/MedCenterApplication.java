package com.medCenter.medCenter;

import com.medCenter.medCenter.exception.LoginException;
import com.medCenter.medCenter.model.entity.User;

import com.medCenter.medCenter.service.impl.UserServiceImpl;




import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void registration() throws Exception {
//        Assertions.assertNotNull(userService);
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        Assertions.assertNotNull(userService);
//
//        userService.registration("testUsername", "testMail", "testLogin", "testPassword", "testRole", null, encoder);
//        User user = userService.findByLoginForReg("testLogin");
//
//        Assertions.assertNotNull(user);

    }

    @Test
    public void registrationException () throws LoginException {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//       Assert.assertNotNull(userService);
//        userService.registration("testUsername", "testMail", "testLogin", "testPassword", "testRole", encoder);
//        userService.registration("testUsername", "testMail", "testLogin", "testPassword", "testRole", encoder);
    }



}
