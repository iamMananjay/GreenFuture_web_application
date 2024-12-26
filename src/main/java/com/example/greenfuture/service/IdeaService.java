package com.example.greenfuture.service;

import com.example.greenfuture.model.*;
import com.example.greenfuture.repository.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IdeaService {

    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CommentRepository commentRepository;

    private final String UPLOAD_DIR = "uploads/"; // Base directory for file uploads
    private final String STORAGE_DIR = "/path/to/storage"; // Update this to your file storage location


    // Create a new Idea, including file upload
    public Idea createIdea(Idea idea, List<MultipartFile> files) {
        // Save the idea first
        Idea savedIdea = ideaRepository.save(idea);

        // Handle files if they exist
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileName = saveFile(savedIdea, file); // Save the file to disk
                idea.getAttachedFiles().add(fileName); // Add file path to idea's list
            }
        }

        // Save the idea with attached files
        return ideaRepository.save(savedIdea);
    }


    // Update an existing Idea with new files
    public Idea updateIdea(Long id, Idea updatedIdea, List<MultipartFile> files) {
        Idea idea = ideaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found"));

        // Update the idea fields
        idea.setTitle(updatedIdea.getTitle());
        idea.setDescription(updatedIdea.getDescription());
        idea.setStatus(updatedIdea.getStatus());
        idea.setVoteCount(updatedIdea.getVoteCount());
        idea.setSubmittedBy(updatedIdea.getSubmittedBy());

        // Check if there is an existing file associated with this idea
        if (idea.getAttachedFiles() != null && !idea.getAttachedFiles().isEmpty()) {
            // If there's an existing file, delete it before adding the new one
            String oldFileName = idea.getAttachedFiles().get(0); // Assuming only one file per idea
            deleteFile(idea, oldFileName); // Call method to delete the file
        }

        // Process file attachments
        if (files != null) {
            files.forEach(file -> {
                String fileName = saveFile(idea, file); // Save the file
                idea.getAttachedFiles().clear(); // Clear the old file list (if needed)
                idea.getAttachedFiles().add(fileName); // Add the file path to the idea's attachedFiles list
            });
        }

        return ideaRepository.save(idea); // Save the updated idea
    }
    private void deleteFile(Idea idea, String fileName) {
        try {
            String ideaDir = UPLOAD_DIR + idea.getId(); // Directory specific to the idea
            Path filePath = Paths.get(ideaDir, fileName);

            // Check if the file exists, then delete it
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Deleted file: " + fileName); // Log the file deletion
            } else {
                System.out.println("File not found: " + fileName); // Log if file not found
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }



    public Idea getIdeaById(Long id) {
        return ideaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Idea not found"));
    }

    public List<Idea> getAllIdeas() {
        return ideaRepository.findAll();
    }

    public void deleteIdea(Long id) {
        // Fetch the Idea from the database
        Idea idea = ideaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found"));


        // Delete all attached files of the idea
        if (idea.getAttachedFiles() != null && !idea.getAttachedFiles().isEmpty()) {
            String ideaDir = UPLOAD_DIR + idea.getId(); // Directory specific to the idea
            for (String fileName : idea.getAttachedFiles()) {
                try {
                    Path filePath = Paths.get(ideaDir, fileName);

                    // Check if the file exists, then delete it
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                        System.out.println("Deleted file: " + fileName); // Log the file deletion
                    } else {
                        System.out.println("File not found: " + fileName); // Log if file not found
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete file: " + fileName, e);
                }
            }
        }

        // Try to delete the idea from the database
        try {
            ideaRepository.delete(idea);
        } catch (DataIntegrityViolationException e) {
            // Handle the case where the idea is still referenced in another table due to a foreign key
            throw new RuntimeException("Please remove the idea from projects or team before deleting.");
        }
    }



    public void voteForIdea(Long ideaId, String userEmail) {
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new RuntimeException("Idea not found"));

        // Check if user already voted
        boolean alreadyVoted = idea.getVotes().stream()
                .anyMatch(vote -> vote.getUserEmail().equals(userEmail));

        if (alreadyVoted) {
            throw new RuntimeException("User has already voted for this idea");
        }

        Vote vote = new Vote();
        vote.setIdea(idea);
        vote.setUserEmail(userEmail);

        voteRepository.save(vote);

        // Increment vote count
        idea.setVoteCount(idea.getVoteCount() + 1);
        ideaRepository.save(idea);
    }

    public void unvoteIdea(Long ideaId, String userEmail) {
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new RuntimeException("Idea not found"));

        Vote vote = voteRepository.findByIdeaIdAndUserEmail(ideaId, userEmail)
                .orElseThrow(() -> new RuntimeException("Vote not found"));

        voteRepository.delete(vote);

        // Decrement vote count
        idea.setVoteCount(idea.getVoteCount() - 1);
        ideaRepository.save(idea);
    }

    public boolean hasUserVoted(Long ideaId, String userEmail) {
        // Fetch the idea by id
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found with id: " + ideaId));

        // Check if the user has voted on this idea
        return idea.getVotes().stream()
                .anyMatch(vote -> vote.getUserEmail().equals(userEmail));
    }

    public void addCommentToIdea(Long ideaId, String content, String commentedBy) {
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCommentedBy(commentedBy);
        comment.setIdea(idea);

        commentRepository.save(comment);
    }

    public List<Comment> getCommentsForIdea(Long ideaId) {
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new ResourceNotFoundException("Idea not found"));
        return idea.getComments();
    }

    public org.springframework.core.io.Resource getAttachment(Long ideaId, String filename) {
        Path filePath = Paths.get(UPLOAD_DIR + ideaId + "/" + filename);
        if (!Files.exists(filePath)) {
            throw new RuntimeException("File not found");
        }

        try {
            return new org.springframework.core.io.UrlResource(filePath.toUri());
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }

    // Method for saving the file to disk
    private String saveFile(Idea idea, MultipartFile file) {
        try {
            String ideaDir = UPLOAD_DIR + idea.getId(); // Directory specific to the idea
            Path directoryPath = Paths.get(ideaDir);

            // Ensure the directory exists
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath); // Create directory if not already exists
            }

            // Save the file with the original file name
            String fileName = file.getOriginalFilename(); // Get the original file name
            Path filePath = directoryPath.resolve(fileName); // Define file path

            // Save the file to the defined path
            Files.write(filePath, file.getBytes());

            return fileName; // Return the saved file name (which will be stored in the database)
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }


    // Method to get the file
    public Resource downloadFile(Long ideaId, String fileName) throws IOException {
        // Construct the path where the file is saved
        Path filePath = Paths.get(UPLOAD_DIR, String.valueOf(ideaId), fileName); // Directory + file name

        // Check if the file exists
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("File not found on server: " + fileName);
        }

        // Return the file as a Resource (no need to cast)
        return new FileSystemResource(filePath); // No casting needed
    }



}
