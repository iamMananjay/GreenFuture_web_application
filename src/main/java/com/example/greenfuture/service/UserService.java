package com.example.greenfuture.service;


import com.example.greenfuture.model.Users;
import com.example.greenfuture.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Optional<Users> getUserById(Integer id) {
        return usersRepository.findById(id);
    }



    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Users createUser(Users user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usersRepository.save(user);
    }

    public Users updateUser(Integer id, Users userDetails) {
        return usersRepository.findById(id)
                .map(user -> {
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    user.setContact(userDetails.getContact());
                    user.setGender(userDetails.getGender());
                    user.setStatus(userDetails.getStatus());
                    user.setUserRole(userDetails.getUserRole());
                    // Update other fields as necessary
                    return usersRepository.save(user);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public void deleteUser(Integer id) {
        usersRepository.deleteById(id);
    }
}

