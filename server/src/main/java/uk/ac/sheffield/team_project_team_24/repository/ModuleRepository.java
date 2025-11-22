package uk.ac.sheffield.team_project_team_24.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.sheffield.team_project_team_24.domain.module.Module;

public interface ModuleRepository extends JpaRepository<Module, String> {

}
