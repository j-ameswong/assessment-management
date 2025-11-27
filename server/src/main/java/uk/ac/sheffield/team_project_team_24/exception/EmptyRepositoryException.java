package uk.ac.sheffield.team_project_team_24.exception;

public class EmptyRepositoryException extends RuntimeException {
    public EmptyRepositoryException(String repositoryName) {
        super("The repository " + repositoryName + " has no entries");
    }
}
