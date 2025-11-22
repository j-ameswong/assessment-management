package uk.ac.sheffield.team_project_team_24.domain.user;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class TeachingStaffId implements Serializable {
  // Primary key
  private Long id;
  // private String moduleCode; TODO: Replace with List<Modules>
  // private String roleName;
}
