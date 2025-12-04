package uk.ac.sheffield.team_project_team_24.domain.assessment;

public enum AssessmentRole {
    SETTER("assessment"),
    CHECKER("assessment"),
    EXTERNAL_EXAMINER("assessment"),
    EXAMS_OFFICER("user"),
    MODERATOR("module"),
    SYSTEM("none"),
    ADMIN("user"),
    ANY("none");

    public String source;

    private AssessmentRole(String source) {
        this.source = source;
    }
}
