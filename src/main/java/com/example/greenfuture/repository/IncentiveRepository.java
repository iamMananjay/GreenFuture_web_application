package com.example.greenfuture.repository;

import com.example.greenfuture.model.Incentive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncentiveRepository extends JpaRepository<Incentive, Long> {
}
