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
  // The way this composite key is implemented,
  // there can only be one row per member of staff,
  // which allows multiple instances of the same role,
  // but never multiple roles for one staff member
  @EmbeddedId
  private ModuleStaffId id;

  @ManyToOne
  @MapsId("moduleId")
  private Module module;

  @ManyToOne
  @MapsId("staffId")
  private User user;

  @Enumerated(EnumType.STRING)
  private ModuleRole moduleRole;

  public ModuleStaff(Module module, User user, ModuleRole moduleRole) {
    this.module = module;
    this.user = user;
    this.moduleRole = moduleRole;
    this.id = new ModuleStaffId(user.getId(), module.getId());
  }
}
