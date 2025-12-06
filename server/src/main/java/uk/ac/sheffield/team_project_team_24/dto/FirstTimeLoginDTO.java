package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirstTimeLoginDTO {
    private Long id;
    private UserRole role;
    private String token;
}
