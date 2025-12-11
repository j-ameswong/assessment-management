package uk.ac.sheffield.team_project_team_24.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.user.User;

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

        if (user.isDeleted()) {
            this.forename = "<deleted";
            this.surname = "user>";
            this.email = null;
            this.status = "DELETED";
        } else {
            this.forename = user.getForename();
            this.surname = user.getSurname();
            this.email = user.getEmail();
            this.status = "ACTIVE";
        }
    }

}

// UserDTO for transferring user info for request/response (password field is
// excluded)
