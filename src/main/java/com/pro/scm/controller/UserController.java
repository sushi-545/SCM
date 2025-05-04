package com.pro.scm.controller;

import com.pro.scm.entities.User;
import com.pro.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pro.scm.helper.Helper;

@Controller
@RequestMapping("/user")
public class UserController {



    @RequestMapping(path = "/dashboard", method = RequestMethod.GET)
    public String userDashboard() {
        return "user/dashboard";
    }

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public String userProfile(Authentication authentication) {


        return "user/profile";
    }
}
