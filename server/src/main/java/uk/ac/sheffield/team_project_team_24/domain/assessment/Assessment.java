package uk.ac.sheffield.team_project_team_24.domain.assessment;

import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import jakarta.persistence.*;
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
  private long assessmentId;

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
