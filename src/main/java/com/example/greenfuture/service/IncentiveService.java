package com.example.greenfuture.service;

import com.example.greenfuture.model.Incentive;
import com.example.greenfuture.repository.IncentiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncentiveService {

    @Autowired
    private IncentiveRepository incentiveRepository;

    public List<Incentive> getAllIncentives() {
        return incentiveRepository.findAll();
    }

    public Incentive createIncentive(Incentive incentive) {
        return incentiveRepository.save(incentive);
    }

    public Incentive updateIncentive(Long id, Incentive incentiveDetails) {
        Incentive incentive = incentiveRepository.findById(id).orElseThrow(() -> new RuntimeException("Incentive not found"));
        incentive.setType(incentiveDetails.getType());
        incentive.setAmount(incentiveDetails.getAmount());
        incentive.setTeam(incentiveDetails.getTeam());
        return incentiveRepository.save(incentive);
    }

    public void deleteIncentive(Long id) {
        incentiveRepository.deleteById(id);
    }
}
