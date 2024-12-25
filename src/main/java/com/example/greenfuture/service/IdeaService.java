package com.example.greenfuture.service;

import com.example.greenfuture.model.Idea;
import com.example.greenfuture.model.Vote;
import com.example.greenfuture.repository.IdeaRepository;
import com.example.greenfuture.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdeaService {

    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private VoteRepository voteRepository;

    public Idea createIdea(Idea idea) {
        return ideaRepository.save(idea);
    }

    public Idea getIdeaById(Long id) {
        return ideaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Idea not found"));
    }

    public List<Idea> getAllIdeas() {
        return ideaRepository.findAll();
    }

    public Idea updateIdea(Long id, Idea updatedIdea) {
        Idea idea = ideaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Idea not found"));
        idea.setTitle(updatedIdea.getTitle());
        idea.setDescription(updatedIdea.getDescription());
        idea.setStatus(updatedIdea.getStatus());
        idea.setVoteCount(updatedIdea.getVoteCount());
        idea.setSubmittedBy(updatedIdea.getSubmittedBy());
        idea.setSubmissionDate(updatedIdea.getSubmissionDate());
        return ideaRepository.save(idea);
    }

    public void deleteIdea(Long id) {
        Idea idea = ideaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Idea not found"));
        ideaRepository.delete(idea);
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
        for (Vote vote : idea.getVotes()) {
            if (vote.getUserEmail().equals(userEmail)) {
                return true; // User has already voted
            }
        }
        return false; // User has not voted
    }

}

