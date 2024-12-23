package com.example.greenfuture.repository;

import com.example.greenfuture.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
