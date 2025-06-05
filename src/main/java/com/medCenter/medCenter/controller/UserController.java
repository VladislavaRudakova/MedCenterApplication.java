package com.medCenter.medCenter.controller;


import com.medCenter.medCenter.dto.UserDto;
import com.medCenter.medCenter.exception.LoginException;
import com.medCenter.medCenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @PostMapping(value = "/registration")
    public String registration(@ModelAttribute UserDto userDto, @RequestParam(required = false) Integer personalJobId, Model model) {
        try {
            userService.registration(userDto, personalJobId, encoder);
        } catch (LoginException e) {
            e.getLocalizedMessage();
            model.addAttribute("exception", e.getLocalizedMessage());
            return "loginPage"; //with message about error
        }
        if (personalJobId != null) { //if not null it is registration of personal by admin
            model.addAttribute("personalJobId", personalJobId);
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
