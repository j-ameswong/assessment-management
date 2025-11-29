package uk.ac.sheffield.team_project_team_24.exception.user;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException(String message) {
        super(message);
    }
}
