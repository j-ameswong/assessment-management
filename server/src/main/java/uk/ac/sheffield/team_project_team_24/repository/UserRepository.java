package uk.ac.sheffield.team_project_team_24.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import uk.ac.sheffield.team_project_team_24.domain.user.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = :email")
    List<User> findAllByEmailQuery(@Param("email") String email);

    List<User> findAllByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    List<User> findAllByRole(UserRole userRole);

    Optional<User> findByRole(UserRole userRole);
}
