package uk.ac.sheffield.team_project_team_24.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Usr")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String forename;

  @Column(nullable = false)
  private String surname;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  public User(String forename, String surname, String email, String password, UserRole role) {
    this.forename = forename;
    this.surname = surname;
    this.email = email;
    this.password = password;
    this.role = role;
  }
}
