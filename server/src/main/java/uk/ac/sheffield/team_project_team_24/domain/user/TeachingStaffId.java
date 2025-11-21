package uk.ac.sheffield.team_project_team_24.domain.user;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class TeachingStaffId implements Serializable {
  private long userId;
  // private String moduleCode; TODO: Replace with List<Modules>
  private String roleName;
}
