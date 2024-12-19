package com.example.greenfuture.repository;

import com.example.greenfuture.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
