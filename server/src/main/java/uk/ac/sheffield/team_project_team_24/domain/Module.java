package uk.ac.sheffield.team_project_team_24.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Module")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module {

    // Primary Key
    @Id
    private String moduleCode;

    @Column(unique = true, nullable = false)
    private String moduleName;

    // Foreign key from User (userID)
    @ManyToOne
    @JoinColumn(name = "leadID")
    private User moduleLead;

    // Foreign key from User (userID)
    @ManyToOne
    @JoinColumn(name = "moderatorID")
    private User moduleModerator;
}