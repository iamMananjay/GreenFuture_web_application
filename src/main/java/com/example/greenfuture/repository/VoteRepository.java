package com.example.greenfuture.repository;

import com.example.greenfuture.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByIdeaIdAndUserEmail(Long ideaId, String userEmail);
}

