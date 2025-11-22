package uk.ac.sheffield.team_project_team_24.domain.module;

import uk.ac.sheffield.team_project_team_24.domain.user.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Join entity between User and Module
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleStaff {
  @EmbeddedId
  private ModuleStaffId id;

  @ManyToOne
  @MapsId("moduleId")
  private Module module;

  @ManyToOne
  @MapsId("staffId")
  private User user;

  @Enumerated(EnumType.STRING)
  private ModuleRole role;
}
