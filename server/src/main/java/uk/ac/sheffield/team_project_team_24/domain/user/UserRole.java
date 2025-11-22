package uk.ac.sheffield.team_project_team_24.domain.user;

public enum UserRole {
  TEACHING_SUPPORT_TEAM("Administrative team responsible for the app"),
  ACADEMIC_STAFF("Academic staff"),
  EXAMS_OFFICER("Overseer for typically undergraduate/postgraduate year");

  public String description;

  private UserRole(String description) {
    this.description = description;
  }
}
