package com.example.greenfuture.repository;


import com.example.greenfuture.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    // Additional query methods can be defined here if needed

    // Method to find user by username
//    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);

}

