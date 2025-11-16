package uk.ac.sheffield.team_project_team_24.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.sheffield.team_project_team_24.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = :username")
    List<User> findAllByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);

    boolean existsUserByUsername(String username);
}
