package com.pro.scm.controller;

import com.pro.scm.entities.User;
import com.pro.scm.helper.Helper;
import com.pro.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RouteController {

    @Autowired
    UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {

        System.out.println("inside addLoggedInUserInformation");
        if(authentication == null) {
            return;
        }
        String userName = Helper.getEmailIdOfLoggedInUser(authentication);

        System.out.println("username-->"+userName);
        User user = userService.getUserByEmail(userName);

        model.addAttribute("loggedInUser", user);




    }

}
