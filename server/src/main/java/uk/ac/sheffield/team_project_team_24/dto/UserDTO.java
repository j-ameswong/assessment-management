package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.user.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String forename;
    private String surname;
    private String email;
    private String status;

    public UserDTO(User user) {
        this.id = user.getId();
        this.forename = user.getForename();
        this.surname = user.getSurname();
        this.email = user.getEmail();
    }
}

// UserDTO for transferring user info for request/response (password field is
// excluded)
