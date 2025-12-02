package uk.ac.sheffield.team_project_team_24.exception.assessment;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AssessmentNotFoundException extends ResponseStatusException {
    public AssessmentNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND,
                "Assessment with id " + id + " not found");
    }
}
