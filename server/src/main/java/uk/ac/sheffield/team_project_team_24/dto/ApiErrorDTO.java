package uk.ac.sheffield.team_project_team_24.dto;

import java.util.List;
import java.util.Map;

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

    // Getters and setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Map<String, List<String>> getErrors() { return errors; }
    public void setErrors(Map<String, List<String>> errors) { this.errors = errors; }
}

