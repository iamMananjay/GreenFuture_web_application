package com.example.greenfuture.repository;

import com.example.greenfuture.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    // Custom method to find regions by userId

    List<Region> findByMembersId(@Param("userId") Integer userId);


}
