package uk.ac.sheffield.team_project_team_24.dto;
import lombok.Getter;
import lombok.Setter;
import uk.ac.sheffield.team_project_team_24.domain.user.*;

@Getter
@Setter
public class UserCreationDTO {
    private String email;
    private String forename;
    private UserRole role;
    private Long id;
}
