package com.veeryash.ppmtool.services;

import com.veeryash.ppmtool.domain.User;
import com.veeryash.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.veeryash.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public User saveUser(User newUser) {
        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());
            // username has to be unique
            // Make sure that password and confirmPassword match
            // We dont persist or show the confirmPassword

            // clear the confirmPassword
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);

        } catch (Exception e) {
            throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists.");
        }
    }
}
