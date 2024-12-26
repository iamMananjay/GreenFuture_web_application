package com.example.greenfuture.controller;

import org.springframework.core.io.Resource;

import com.example.greenfuture.model.Comment;
import com.example.greenfuture.model.Idea;
import com.example.greenfuture.service.IdeaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
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
    public ResponseEntity<Idea> createIdea(
            @RequestParam("idea") String ideaJson,
            MultipartHttpServletRequest request) throws Exception {

        // Convert the JSON string back to the Idea object
        ObjectMapper objectMapper = new ObjectMapper();
        Idea idea = objectMapper.readValue(ideaJson, Idea.class);

        // Get the list of files from the request
        List<MultipartFile> files = request.getFiles("files");

        // Ensure files are available and properly handled
        if (files != null && !files.isEmpty()) {
            // Process the files as needed (e.g., save to a server or database)
        }
        // Handle the idea and files
        Idea createdIdea = ideaService.createIdea(idea, files);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdIdea);
    }


    @PutMapping("/api/ideas/{id}")
    public ResponseEntity<Idea> updateIdea(
            @PathVariable Long id,
            @RequestParam("idea") String ideaJson, // Receive the idea as a JSON string
            MultipartHttpServletRequest request) throws Exception {

        // Convert the JSON string back to the Idea object
        ObjectMapper objectMapper = new ObjectMapper();
        Idea updatedIdea = objectMapper.readValue(ideaJson, Idea.class);

        // Get the list of files from the request
        List<MultipartFile> files = request.getFiles("files");

        // Ensure files are available and properly handled
        if (files != null && !files.isEmpty()) {
            // Process the files as needed (e.g., save to a server or database)
        }

        // Call the service to update the idea (make sure it handles both idea updates and file updates)
        Idea updated = ideaService.updateIdea(id, updatedIdea, files);

        // Return the updated idea
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/api/ideas/{id}")
    public ResponseEntity<?> deleteIdea(@PathVariable Long id) {
        try {
            ideaService.deleteIdea(id);
            return ResponseEntity.noContent().build();  // Success response (No Content)
        } catch (RuntimeException e) {
            // Catch the custom RuntimeException and return the message to frontend
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));  // Return the error message
        }
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
    @PostMapping("/api/ideas/{id}/comments")
    public ResponseEntity<Void> addComment(
            @PathVariable Long id,
            @RequestParam String content,
            @RequestParam String commentedBy) {
        ideaService.addCommentToIdea(id, content, commentedBy);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/ideas/{id}/comments")
    public ResponseEntity<List<Comment>> getCommentsByIdeaId(@PathVariable Long id) {
        List<Comment> comments = ideaService.getCommentsForIdea(id);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/api/ideas/{id}/attachments/{filename}")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable Long id,
            @PathVariable String filename) {
        Resource file = (Resource) ideaService.getAttachment(id, filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(file);
    }

    @GetMapping("/api/ideas/{ideaId}/files/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long ideaId, @PathVariable String fileName) {
        try {
            // Fetch the file from the service
            Resource file = ideaService.downloadFile(ideaId, fileName);

            // Set the content disposition header to indicate it's an attachment
            String contentDisposition = "attachment; filename=\"" + fileName + "\"";
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(file);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found", e);
        }
    }






}

