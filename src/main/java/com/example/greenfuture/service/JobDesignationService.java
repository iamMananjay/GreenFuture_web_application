package com.example.greenfuture.service;

import com.example.greenfuture.model.JobDesignation;
import com.example.greenfuture.repository.JobDesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobDesignationService {
    @Autowired
    private JobDesignationRepository repository;

    public List<JobDesignation> getAll() {
        return repository.findAll();
    }

    public JobDesignation getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }

    public JobDesignation save(JobDesignation jobDesignation) {
        return repository.save(jobDesignation);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

