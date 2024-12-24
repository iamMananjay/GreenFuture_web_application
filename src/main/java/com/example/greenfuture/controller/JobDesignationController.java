package com.example.greenfuture.controller;

import com.example.greenfuture.model.JobDesignation;
import com.example.greenfuture.service.JobDesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-designations")
public class JobDesignationController {
    @Autowired
    private JobDesignationService service;

    @GetMapping
    public List<JobDesignation> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public JobDesignation getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public JobDesignation create(@RequestBody JobDesignation jobDesignation) {
        return service.save(jobDesignation);
    }

    @PutMapping("/{id}")
    public JobDesignation update(@PathVariable Long id, @RequestBody JobDesignation jobDesignation) {
        jobDesignation.setId(id);
        return service.save(jobDesignation);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

