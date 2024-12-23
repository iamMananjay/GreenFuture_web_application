package com.example.greenfuture.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "incentive")
@Data
public class Incentive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Column(name = "type")
    private String type; // Bonus or Reward

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team; // Foreign key to Team
}
