package uk.ac.sheffield.team_project_team_24.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Assessment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long assessmentID;

    @Column(unique = true, nullable = false)
    private String assessmentName;

    @Column(nullable = false)
    private String assessmentType;

    @ManyToOne
    @JoinColumn(name = "moduleCode")
    private Module module;

    @ManyToOne
    @JoinColumn(name = "checkerID")
    private User checker;
}