package uk.ac.sheffield.team_project_team_24.domain.assessment;

public enum AssessmentStages {
    // coursework
    CW_SPEC_CREATED(AssessmentRole.SETTER, "Specification created by assessment setter"),
    CW_SPEC_CHECKED(AssessmentRole.CHECKER, "Specification checked by assessment checker"),
    CW_SPEC_MODIFICATION(AssessmentRole.SETTER, "(If required) Modifications are made by assessment setter"),
    CW_SPEC_RELEASED(AssessmentRole.ANY, "Specification released to students"),
    CW_DEADLINE_PASSED(AssessmentRole.SYSTEM, "Coursework deadline passed"),
    CW_STANDARDISATION_DONE(AssessmentRole.ANY, "Marking standardisation done"),
    CW_MARKING_DONE(AssessmentRole.ANY, "Marking done"),
    CW_MODERATION_DONE(AssessmentRole.MODERATOR, "Marking moderation completed"),
    CW_FEEDBACK_RETURNED(AssessmentRole.ANY, "Coursework feedback returned to students"),

    // coursework
    EXAM_CREATED(AssessmentRole.SETTER, "Exam created by assessment setter"),
    EXAM_CHECKED(AssessmentRole.CHECKER, "Paper checked by assessment checker"),
    EXAM_MODIFICATION(AssessmentRole.SETTER, "(If required) Modifications are made by assessment setter"),
    EXAM_CHECKED_EXAMS_OFFICER(AssessmentRole.CHECKER, "Paper checked by exams officer"),
    EXAM_MODIFICATION_EXAMS_OFFICER(AssessmentRole.SETTER, "(If required) Modifications are made by assessment setter"),
    EXAM_CHECKED_EXTERNAL_EXAMINER(AssessmentRole.EXTERNAL_EXAMINER,
            "Paper checked by external examiner and feedback is provided"),
    EXAM_MODIFICATION_EXTERNAL_EXAMINER(AssessmentRole.SETTER,
            "Setter responded to feedback and made changes (If required)"),
    EXAM_CHECKED_FINAL(AssessmentRole.EXAMS_OFFICER, "Final check performed by exams officer"),
    EXAM_MODIFICATION_FINAL(AssessmentRole.SETTER, "(If required) Modifications are made by assessment setter"),
    EXAM_TAKES_PLACE(AssessmentRole.SYSTEM, "The exam has taken place"),
    EXAM_STANDARDISATION_DONE(AssessmentRole.ANY, "Marking standardisation done"),
    EXAM_MARKING_DONE(AssessmentRole.ANY, "Marking done"),
    EXAM_MARKING_CHECKED(AssessmentRole.ADMIN, "Marking checked by admin team"),
    EXAM_MODERATION_DONE(AssessmentRole.MODERATOR, "Marking moderation completed"),

    // test
    // TODO: OPTIONAL: when creating test, specify team marking or not, and specify
    // autograded or not
    TEST_CREATED(AssessmentRole.SETTER, "Test created by assessment setter"),
    TEST_CHECKED(AssessmentRole.CHECKER, "Test checked by assessment checker"),
    TEST_MODIFICATION(AssessmentRole.SETTER, "(If required) Modifications are made by assessment setter"),
    TEST_TAKES_PLACE(AssessmentRole.SYSTEM, "The test has taken place"),
    TEST_STANDARDISATION_DONE(AssessmentRole.ANY, "Marking standardisation done"),
    TEST_MARKING_DONE(AssessmentRole.ANY, "Marking done"),
    TEST_MODERATION_DONE(AssessmentRole.MODERATOR, "Marking moderation completed"),
    TEST_RESULTS_RETURNED(AssessmentRole.ANY, "Test results returned to students");

    public AssessmentRole actor;
    public String description;

    private AssessmentStages(AssessmentRole actor, String description) {
        this.actor = actor;
        this.description = description;
    }
}
