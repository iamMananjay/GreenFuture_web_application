package com.example.greenfuture.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "project")
@Data
public class Project {
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

    public Idea getIdea() {
        return idea;
    }

    public void setIdea(Idea idea) {
        this.idea = idea;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "idea_id", referencedColumnName = "id")
    private Idea idea; // Foreign key to Idea

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team; // Foreign key to Team

    @Column(name = "stage")
    private String stage; // e.g., "ONGOING", "PENDING", "COMPLETE"
}
