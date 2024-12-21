package com.example.greenfuture.repository;

import com.example.greenfuture.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByMembersId(Integer memberId);

}
