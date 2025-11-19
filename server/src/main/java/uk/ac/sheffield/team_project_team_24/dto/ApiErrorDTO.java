package uk.ac.sheffield.team_project_team_24.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class ApiErrorDTO {
    private int status;
    private String message;
    private Map<String, List<String>> errors;

    public ApiErrorDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiErrorDTO(int status, String message, Map<String, List<String>> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

}

