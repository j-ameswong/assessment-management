package uk.ac.sheffield.team_project_team_24.repository;

import uk.ac.sheffield.team_project_team_24.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
