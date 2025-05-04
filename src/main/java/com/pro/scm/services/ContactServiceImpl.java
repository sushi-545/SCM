package com.pro.scm.services;

import com.pro.scm.entities.Contact;
import com.pro.scm.entities.User;
import com.pro.scm.helper.ResourceNotFoundException;
import com.pro.scm.repositories.ContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepo contactRepo;

    @Override
    public Contact save(Contact contact) {

        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {

        var contactOld = contactRepo.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setPicture(contact.getPicture());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setCloudinaryPublicImageId(contact.getCloudinaryPublicImageId());

        return contactRepo.save(contactOld);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));

    }

    @Override
    public void delete(String id) {
        var contact = contactRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
        contactRepo.delete(contact);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);

    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUser(user, pageable);

    }

    @Override
    public Page<Contact> searchByNames(String name, int page, int size, String sortField, String sortDirection, User user) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField));

        return contactRepo.findByUserAndNameContaining(user,name,pageRequest);
    }

    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortField, String sortDirection, User user) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField));

        return contactRepo.findByUserAndEmailContaining(user,email,pageRequest);

    }

    @Override
    public Page<Contact> searchByPhone(String phone, int page, int size, String sortField, String sortDirection, User user) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortField));

        return contactRepo.findByUserAndPhoneNumberContaining(user,phone,pageRequest);

    }
}
