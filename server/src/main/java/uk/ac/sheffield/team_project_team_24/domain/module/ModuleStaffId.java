package uk.ac.sheffield.team_project_team_24.domain.module;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModuleStaffId implements Serializable {
  private Long moduleId;
  private Long staffId;
}
