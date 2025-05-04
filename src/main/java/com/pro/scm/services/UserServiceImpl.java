package com.pro.scm.services;

import com.pro.scm.entities.User;
import com.pro.scm.helper.ResourceNotFoundException;
import com.pro.scm.repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {

        String userId = UUID.randomUUID().toString();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleList(List.of("ROLE_USER"));
        user.setUserId(userId);
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(Long.valueOf(id));
    }

    @Override
    public Optional<User> updateUser(User user) {
         User userDb=userRepo.findById(Long.valueOf(user.getUserId())).orElseThrow(()->  new ResourceNotFoundException("User Not Found."));
         userDb.setEmail(user.getEmail());
         userDb.setName(user.getName());
         userDb.setAbout(user.getAbout());
         userDb.setPhoneNumber(user.getPhoneNumber());
         userDb.setPassword(user.getPassword());
         userDb.setProfilePic(user.getProfilePic());
         userDb.setEnabled(user.isEnabled());
         userDb.setEmailVerified(user.isEmailVerified());
         userDb.setPhoneVerified(user.isPhoneVerified());
         userDb.setProvider(user.getProvider());
         userDb.setProviderUserId(user.getProviderUserId());

         User updatedUser=userRepo.save(userDb);

         return Optional.ofNullable(updatedUser);


    }

    @Override
    public void deleteUser(String id) {
        User userDb=userRepo.findById(Long.valueOf(id)).orElseThrow(()->  new ResourceNotFoundException("User Not Found."));
        userRepo.delete(userDb);

    }

    @Override
    public boolean isUserExist(String userId) {
        User userDb=userRepo.findById(Long.valueOf(userId)).orElse(null);
        return userDb!=null ? true : false;

    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User userDb= userRepo.findByEmail(email).orElse(null);
        return userDb!=null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);


    }
}
