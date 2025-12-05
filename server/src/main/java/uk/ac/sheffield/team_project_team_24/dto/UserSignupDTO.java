package uk.ac.sheffield.team_project_team_24.dto;

import jakarta.validation.constraints.Size;

import uk.ac.sheffield.team_project_team_24.domain.user.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;

@Setter
@Getter
public class UserSignupDTO {

    @NotNull(message = "cannot be null")
    @Size(min = 5, max = 30, message = "must be between 5 and 30 characters in length")
    @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "only letters, digits and underscores allowed")
    private String username;

    @NotNull(message = "cannot be null")
    @Size(min = 8, message = "must be at least 8 characters in length")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$", message = "Password must contain at least one digit, one lowercase, one uppercase letter, one special character, and no spaces")
    private String password;

    private UserRole role;

    public static User toEntity(UserSignupDTO dto) {
        User user = new User();
        user.setEmail(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());

        return user;
    }
}
