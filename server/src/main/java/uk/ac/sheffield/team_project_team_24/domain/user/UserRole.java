package uk.ac.sheffield.team_project_team_24.domain.user;

public enum UserRole {
    ADMIN("Administrative team responsible for the app, also know as the Teaching Support Staff"),
    ACADEMIC_STAFF("Academic staff"),
    EXAMS_OFFICER("Overseer for typically undergraduate/postgraduate year"),
    EXTERNAL_EXAMINER("External Examiner from another university");

    public String description;

    private UserRole(String description) {
        this.description = description;
    }
}
