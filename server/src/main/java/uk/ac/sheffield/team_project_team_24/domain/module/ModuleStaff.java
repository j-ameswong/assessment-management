package uk.ac.sheffield.team_project_team_24.domain.module;

import uk.ac.sheffield.team_project_team_24.domain.user.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ModuleStaff {

  @EmbeddedId
  private ModuleStaffId id;

  @ManyToOne
  @MapsId("moduleId")
  private Module module;

  @ManyToOne
  @MapsId("id")
  private User user;

  @Enumerated(EnumType.STRING)
  private ModuleRole role;

  // public ModuleStaff(Module module, TeachingStaff teachingStaff, ModuleRole
  // role) {
  // this.module = module;
  // this.teachingStaff = teachingStaff;
  // this.role = role;
  // this.id = new ModuleStaffId(module.getId(),
  // teachingStaff.getStaffId().getId());
  // }
}
