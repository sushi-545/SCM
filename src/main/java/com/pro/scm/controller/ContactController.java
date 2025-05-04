package com.pro.scm.controller;

import com.pro.scm.entities.Contact;
import com.pro.scm.entities.User;
import com.pro.scm.forms.ContactForm;
import com.pro.scm.forms.ContactSearchForm;
import com.pro.scm.helper.Helper;
import com.pro.scm.helper.Message;
import com.pro.scm.helper.MessageType;
import com.pro.scm.services.ContactService;
import com.pro.scm.services.ImageService;
import com.pro.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user/contact")
public class ContactController {

    @Autowired
    ContactService contactService;

    @Autowired
    ImageService imageService;

    @Autowired
    UserService userService;

    @RequestMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute("contactForm") ContactForm contactForm, BindingResult bindingResult, Authentication authentication, HttpSession httpSession, Model model) {

        if(bindingResult.hasErrors()) {

            bindingResult.getAllErrors().forEach(err -> System.out.println("erros:::"+err));

            httpSession.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            model.addAttribute("contactForm", contactForm); // Add this!

            return "user/add_contact";
        }
        System.out.println("Inside save contact....");
        String username = Helper.getEmailIdOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        String filename = UUID.randomUUID().toString();

        String fileURL= imageService.uploadImage(contactForm.getContactImage(),filename);
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setUser(user);
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPicture(fileURL);
        contact.setCloudinaryPublicImageId(filename);
        contactService.save(contact);

        httpSession.setAttribute("message",
                Message.builder()
                        .content("You have successfully added a new contact")
                        .type(MessageType.green)
                        .build());
        return "redirect:/user/contact/add";
    }


    @RequestMapping("/")
    public String viewContact(Model model, Authentication auth,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "3") int size,
                              @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                              @RequestParam(value = "direction", defaultValue = "asc") String direction
                              ){

        String username = Helper.getEmailIdOfLoggedInUser( auth);
        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", "3");
        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/contacts";
    }

    @RequestMapping("/search")
    public String searchHandler(

            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,Authentication auth)

    {

        size = size < 1 ? 1 : size;
        System.out.println("field:"+contactSearchForm.getField()+" value:"+contactSearchForm.getValue()+" page:"+page+" size:"+size+" sortBy:"+sortBy+" direction:"+direction);

        String username = Helper.getEmailIdOfLoggedInUser( auth);
        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact = null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")) {
           pageContact= contactService.searchByNames(contactSearchForm.getValue(),page,size,sortBy,direction,user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact= contactService.searchByEmail(contactSearchForm.getValue(),page,size,sortBy,direction,user);

        } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
            pageContact= contactService.searchByPhone(contactSearchForm.getValue(),page,size,sortBy,direction,user);
        } else {
            return "redirect:/user/contact/";
        }

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageSize", size);
        return "user/search";
    }

    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") String contactId,HttpSession httpSession){
        contactService.delete(contactId);

        httpSession.setAttribute("message",Message.builder().content("Contact deleted successfully").type(MessageType.green).build());



        return "redirect:/user/contact/";
    }

    @GetMapping("/view/{contactId}")
    public String updateContactFormView(
            @PathVariable("contactId") String contactId,
            Model model) {

        var contact = contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());
        ;
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        return "user/update_contact_view";
    }

    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId,
                                @Valid @ModelAttribute ContactForm contactForm,
                                BindingResult bindingResult,
                                Model model, HttpSession httpSession) {

        // update the contact
        if (bindingResult.hasErrors()) {
            return "user/update_contact_view";
        }

        var con = contactService.getById(contactId);
        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setFavorite(contactForm.isFavorite());
        con.setWebsiteLink(contactForm.getWebsiteLink());
        con.setLinkedInLink(contactForm.getLinkedInLink());

        // process image:

        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            con.setCloudinaryPublicImageId(fileName);
            con.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);

        }

        var updateCon = contactService.update(con);

        httpSession.setAttribute("message", Message.builder().content("Contact Updated !!").type(MessageType.green).build());

        return "redirect:/user/contact/view/" + contactId;
    }



    }
