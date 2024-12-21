package com.example.greenfuture.controller;


import com.example.greenfuture.model.Region;
import com.example.greenfuture.model.Team;
import com.example.greenfuture.model.Users;
import com.example.greenfuture.repository.RegionRepository;
import com.example.greenfuture.repository.TeamRepository;
import com.example.greenfuture.repository.UsersRepository;
import com.example.greenfuture.service.ResourceNotFoundException;
import com.example.greenfuture.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class UsersController {

    @Autowired
    private UserService userService;

    private UsersRepository usersRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private TeamRepository teamRepository;


    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Integer id, @RequestBody Users userDetails) {
        try {
            Users updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        // Check if the user is associated with any regions
        List<Region> regions = regionRepository.findByMembersId(id);

        if (!regions.isEmpty()) {
            String regionNames = regions.stream()
                    .map(Region::getName)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "message", "User is associated with the following regions: " + regionNames + ". Please remove the user from these regions before deleting."
                    ));
        }

        // Check if the user is associated with any teams
        List<Team> teams = teamRepository.findByMembersId(id);

        if (!teams.isEmpty()) {
            String teamNames = teams.stream()
                    .map(Team::getName)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "message", "User is associated with the following teams: " + teamNames + ". Please remove the user from these teams before deleting."
                    ));
        }

        // If no associations exist, delete the user
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }





}

