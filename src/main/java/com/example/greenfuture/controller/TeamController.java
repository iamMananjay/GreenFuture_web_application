package com.example.greenfuture.controller;

import com.example.greenfuture.model.Team;
import com.example.greenfuture.model.Idea;
import com.example.greenfuture.model.Users;
import com.example.greenfuture.repository.TeamRepository;
import com.example.greenfuture.repository.IdeaRepository;
import com.example.greenfuture.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private UsersRepository usersRepository;

    // Create a new team and associate it with an idea
    @PostMapping
    public Team createTeam(@RequestBody Map<String, Object> requestData) {
        // Extract fields from the request payload
        String teamName = (String) requestData.get("name");
        Map<String, String> ideaData = (Map<String, String>) requestData.get("idea");
        List<Map<String, String>> membersData = (List<Map<String, String>>) requestData.get("members");

        // Ensure the idea exists
        Idea idea = ideaRepository.findById(Long.valueOf(Integer.valueOf(ideaData.get("id"))))
                .orElseThrow(() -> new RuntimeException("Idea not found"));

        // Get the idea creator (submittedBy field)
        Users ideaCreator = usersRepository.findByEmail(idea.getSubmittedBy())
                .orElseThrow(() -> new RuntimeException("Idea creator not found"));


        // Create a set to avoid duplicate members
        Set<Users> membersSet = new HashSet<>();
        membersSet.add(ideaCreator); // Add the idea creator to the set

        // Add team members from the request
        if (membersData != null) {
            for (Map<String, String> memberData : membersData) {
                Integer memberId = Integer.valueOf(memberData.get("id"));
                Users member = usersRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("Team member not found for ID: " + memberId));
                membersSet.add(member); // Add to the set (automatically prevents duplicates)
            }
        }

        // Create and populate the team object
        Team team = new Team();
        team.setName(teamName);
        team.setIdea(idea);
        team.setMembers(new ArrayList<>(membersSet)); // Convert set back to list

        // Save and return the team
        return teamRepository.save(team);
    }


    // Get all teams
    @GetMapping
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // Get a team by ID
    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }
    // Edit a team by ID
    @PutMapping("/{id}")
    public Team updateTeam(@PathVariable Long id, @RequestBody Map<String, Object> requestData) {
        // Find the team by ID
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Update team name
        if (requestData.containsKey("name")) {
            String teamName = (String) requestData.get("name");
            team.setName(teamName);
        }

        // Update associated idea
        if (requestData.containsKey("idea")) {
            Map<String, String> ideaData = (Map<String, String>) requestData.get("idea");
            Idea idea = ideaRepository.findById(Long.valueOf(Integer.valueOf(ideaData.get("id"))))
                    .orElseThrow(() -> new RuntimeException("Idea not found"));
            team.setIdea(idea);
        }

        // Update team members
        if (requestData.containsKey("members")) {
            List<Map<String, String>> membersData = (List<Map<String, String>>) requestData.get("members");
            Set<Users> membersSet = new HashSet<>();

            for (Map<String, String> memberData : membersData) {
                Integer memberId = Integer.valueOf(memberData.get("id"));
                Users member = usersRepository.findById(memberId)
                        .orElseThrow(() -> new RuntimeException("Team member not found for ID: " + memberId));
                membersSet.add(member);
            }

            team.setMembers(new ArrayList<>(membersSet));
        }

        // Save and return the updated team
        return teamRepository.save(team);
    }

    // Delete a team by ID
    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        // Check if the team exists
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Delete the team
        teamRepository.delete(team);
    }


}
