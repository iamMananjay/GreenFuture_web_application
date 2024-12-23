package com.example.greenfuture.controller;

import com.example.greenfuture.model.Incentive;
import com.example.greenfuture.repository.TeamRepository;
import com.example.greenfuture.service.IncentiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incentives")
public class IncentiveController {

    @Autowired
    private IncentiveService incentiveService;
    @Autowired
    private TeamRepository teamRepository;

    @GetMapping
    public List<Incentive> getAllIncentives() {
        return incentiveService.getAllIncentives();
    }

    @PostMapping
    public Incentive createIncentive(@RequestBody Incentive incentive) {
        return incentiveService.createIncentive(incentive);
    }

    @PutMapping("/{id}")
    public Incentive updateIncentive(@PathVariable Long id, @RequestBody Incentive incentive) {
        return incentiveService.updateIncentive(id, incentive);
    }

    @DeleteMapping("/{id}")
    public void deleteIncentive(@PathVariable Long id) {
        incentiveService.deleteIncentive(id);
    }
}
