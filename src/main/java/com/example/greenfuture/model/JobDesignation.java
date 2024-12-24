package com.example.greenfuture.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "job_designations")
@Data
public class JobDesignation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_seq")
    @SequenceGenerator(name = "job_seq", sequenceName = "job_sequence", allocationSize = 1)
    private Long id;

    private String name;
    private Double salary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
// Getters and Setters
}

