package uk.ac.sheffield.team_project_team_24.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdatePasswordDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

}
