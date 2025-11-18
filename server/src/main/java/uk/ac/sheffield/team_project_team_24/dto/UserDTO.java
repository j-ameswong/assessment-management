package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String forename;
    private String surname;
    private String email;
    private String status;
}

// UserDTO for transferring user info for request/response (password field is excluded)
