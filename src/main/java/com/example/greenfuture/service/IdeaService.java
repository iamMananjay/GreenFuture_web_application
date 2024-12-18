package com.example.greenfuture.service;

import com.example.greenfuture.model.Idea;
import com.example.greenfuture.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdeaService {

    @Autowired
    private IdeaRepository ideaRepository;

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
}

