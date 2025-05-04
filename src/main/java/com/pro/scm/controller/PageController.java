package com.pro.scm.controller;

import com.pro.scm.entities.User;
import com.pro.scm.forms.UserForm;
import com.pro.scm.helper.Message;
import com.pro.scm.helper.MessageType;
import com.pro.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("home page handler.");
        model.addAttribute("name","sushant");
        model.addAttribute("title","first-page");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(){
        return "about";
    }

    @RequestMapping("/services")
    public String servicePage(){
        return "service";
    }

    @RequestMapping("/contact")
    public String contactPage(){
        return "contact";
    }

    @RequestMapping(value = "/login")
    public String loginPage(){
        return "login";
    }

    @RequestMapping("/test")
    public String testPage(){
        return "test";
    }

    @RequestMapping("/register")
    public String registerPage(Model model){

        UserForm  userForm = new UserForm();
//        userForm.setAbout("hi");
//        userForm.setName("raghav");
//        userForm.setPhoneNumber("899876");
        model.addAttribute("userForm",userForm);
        return "register";
    }

    @RequestMapping(value = "/do-register" ,method = RequestMethod.POST)
    public String processRegisterForm(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult, HttpSession session)
    {
        if(bindingResult.hasErrors()){

            bindingResult.getAllErrors().forEach(error -> System.out.println("error->"+error.getDefaultMessage()));
            return "register";
        }


        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://www.shutterstock.com/shutterstock/photos/2470054311/display_1500/stock-vector-avatar-gender-neutral-silhouette-vector-illustration-profile-picture-no-image-for-social-media-2470054311.jpg");
        User savedUser= userService.saveUser(user);
        System.out.println("saved user: "+savedUser);

        Message message= Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message",message);
        return "redirect:/register";
    }
}
