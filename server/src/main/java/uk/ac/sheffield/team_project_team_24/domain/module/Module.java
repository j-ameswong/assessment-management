package uk.ac.sheffield.team_project_team_24.domain.module;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Module")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String moduleCode;

  @Column(unique = true)
  private String moduleName;

  @OneToMany(mappedBy = "module", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ModuleStaff> moduleStaff;

  public Module(String moduleCode, String moduleName) {
    this.moduleCode = moduleCode;
    this.moduleName = moduleName;
  }
}
