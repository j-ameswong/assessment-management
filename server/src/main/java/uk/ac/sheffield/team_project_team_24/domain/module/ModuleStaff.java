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
  private ModuleStaffId id = new ModuleStaffId();

  @ManyToOne
  @MapsId("moduleId")
  private Module module;

  @ManyToOne
  @MapsId("teachingStaffId")
  private TeachingStaff staff;

  @Enumerated(EnumType.STRING)
  private ModuleRole role;

  public ModuleStaff(Module module, TeachingStaff staff, ModuleRole role) {
    this.module = module;
    this.staff = staff;
    this.role = role;
    this.id = new ModuleStaffId(module.getId(), staff.getStaffId().getUserId());
  }
}
