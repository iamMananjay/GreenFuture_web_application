package com.example.greenfuture.controller;

import com.example.greenfuture.model.Idea;
import com.example.greenfuture.service.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/ideas")
public class IdeaController {

    @Autowired
    private IdeaService ideaService;

    @GetMapping("/api/ideas")
    public ResponseEntity<List<Idea>> getAllIdeas() {
        return ResponseEntity.ok(ideaService.getAllIdeas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Idea> getIdeaById(@PathVariable Long id) {
        return ResponseEntity.ok(ideaService.getIdeaById(id));
    }

    @PostMapping("/api/ideas")
    public ResponseEntity<Idea> createIdea(@RequestBody Idea idea) {
        Idea createdIdea = ideaService.createIdea(idea);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIdea);
    }

    @PutMapping("/api/ideas/{id}")
    public ResponseEntity<Idea> updateIdea(@PathVariable Long id, @RequestBody Idea updatedIdea) {
        Idea updated = ideaService.updateIdea(id, updatedIdea);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/api/ideas/{id}")
    public ResponseEntity<Void> deleteIdea(@PathVariable Long id) {
        ideaService.deleteIdea(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/ideas/{id}/vote")
    public ResponseEntity<Void> voteForIdea(@PathVariable Long id, @RequestParam String userEmail) {
        ideaService.voteForIdea(id, userEmail);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/ideas/{id}/unvote")
    public ResponseEntity<Void> unvoteIdea(@PathVariable Long id, @RequestParam String userEmail) {
        ideaService.unvoteIdea(id, userEmail);
        return ResponseEntity.ok().build();
    }
    // Check if a user has voted on a specific idea
    @GetMapping("/api/ideas/{id}/vote/check")
    public ResponseEntity<Boolean> checkUserVote(
            @PathVariable Long id,
            @RequestParam String userEmail) {
        boolean hasVoted = ideaService.hasUserVoted(id, userEmail);
        return ResponseEntity.ok(hasVoted);
    }


}

