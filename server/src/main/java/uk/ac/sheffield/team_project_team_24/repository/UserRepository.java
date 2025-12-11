package uk.ac.sheffield.team_project_team_24.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = :email")
    List<User> findAllByEmailQuery(@Param("email") String email);

    List<User> findAllByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    List<User> findAllByRole(UserRole userRole);

    Optional<User> findByRole(UserRole userRole);

    Optional<User> findByForenameAndSurname(String forename, String surname);

    List<User> findAllByDeletedFalse();

    List<User> findAllByRoleAndDeletedFalse(UserRole userRole);

}
