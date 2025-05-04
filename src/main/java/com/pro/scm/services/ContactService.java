package com.pro.scm.services;

import com.pro.scm.entities.Contact;
import com.pro.scm.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ContactService {

    Contact save(Contact contact);
    Contact update(Contact contact);
    List<Contact> getAll();
    Contact getById(String id);
    void delete(String id);
    List<Contact> getByUserId(String userId);

    Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);

    Page<Contact> searchByNames(String name,int page, int size, String sortField, String sortDirection, User user);
    Page<Contact> searchByEmail(String email, int page, int size, String sortField, String sortDirection, User user);
    Page<Contact> searchByPhone(String phone,int page, int size, String sortField, String sortDirection, User user);
}