package uk.ac.sheffield.team_project_team_24.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TeachingStaff")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Table that represents staff roles
// composite key uses userid, their role, module code
public class TeachingStaff {

  // Composite primary key created in teachingStaffId class
  @EmbeddedId
  private TeachingStaffId staffId;

  @MapsId("staffId")
  @OneToOne
  @JoinColumn(name = "id")
  private User user;

}
