package uk.ac.sheffield.team_project_team_24.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "Usr")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  @Column(nullable = false)
  private String forename;

  @NonNull
  @Column(nullable = false)
  private String surname;

  @NonNull
  @Column(unique = true, nullable = false)
  private String email;

  @NonNull
  @Column(nullable = false)
  private String password;

  @NonNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;
}
