package com.example.greenfuture.repository;


import com.example.greenfuture.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    // Additional query methods can be defined here if needed
}

