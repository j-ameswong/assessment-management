package uk.ac.sheffield.team_project_team_24.domain.user;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teachingStaff")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Table that represents staff roles
// composite key uses userid, their role, module code
public class TeachingStaff {

  // Composite primary key created in StaffID class
  @EmbeddedId
  private TeachingStaffId staffId;

}
