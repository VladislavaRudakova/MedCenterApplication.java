package com.medCenter.medCenter.controller;


import com.medCenter.medCenter.dto.UserDto;
import com.medCenter.medCenter.exception.LoginException;
import com.medCenter.medCenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @PostMapping(value = "/registration")
    public String registration(@ModelAttribute UserDto userDto, @RequestParam(required = false) Integer personalJobId) {
        try {
            userService.registration(userDto,personalJobId, encoder);
        }catch (LoginException e){
            e.getLocalizedMessage();
        }
        return "loginPage";
    }

    @GetMapping(value = "/registration") //get registration form
    public String getRegForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "registrationPage";
    }

    @GetMapping(value = "/login")
    public String getLoginForm() {
        return "loginPage";
    }


    @PostMapping(value = "/exit")
    public String exit(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "startPage";
    }


}
