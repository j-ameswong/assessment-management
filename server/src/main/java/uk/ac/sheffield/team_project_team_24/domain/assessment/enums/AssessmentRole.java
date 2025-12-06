package uk.ac.sheffield.team_project_team_24.domain.assessment.enums;

public enum AssessmentRole {
    SETTER(UserSource.ASSESSMENT),
    CHECKER(UserSource.ASSESSMENT),
    EXTERNAL_EXAMINER(UserSource.ASSESSMENT),
    EXAMS_OFFICER(UserSource.USER),
    MODERATOR(UserSource.MODULE),
    SYSTEM(UserSource.NONE),
    ADMIN(UserSource.USER),
    ANY(UserSource.NONE);

    public UserSource source;

    private AssessmentRole(UserSource source) {
        this.source = source;
    }
}
