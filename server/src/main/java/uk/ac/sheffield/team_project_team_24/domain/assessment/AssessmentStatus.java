package uk.ac.sheffield.team_project_team_24.domain.assessment;

public enum AssessmentStatus {
    // Coursework
    CW_SPEC_CREATED,
    CW_SPEC_CHECKED,
    CW_SPEC_MODIFIED,
    CW_SPEC_RELEASED,
    CW_DEADLINE_PASSED,
    CW_STANDARDISATION_DONE,
    CW_MARKING_DONE,
    CW_MODERATION_DONE,
    CW_FEEDBACK_RETURNED,

    // Test
    TEST_CREATED,
    TEST_CHECKED,
    TEST_MODIFIED,
    TEST_TAKEN,
    TEST_STANDARDISATION_DONE,
    TEST_MARKING_DONE,
    TEST_MODERATION_DONE,
    TEST_RESULTS_RETURNED,

    // Exam
    EXAM_CREATED,
    EXAM_CHECKED,
    EXAM_MODIFIED,
    EXAM_OFFICER_CHECKED,
    EXTERNAL_EXAMINER_FEEDBACK,
    SETTER_RESPONSE_SUBMITTED,
    FINAL_EXAM_OFFICER_CHECK,
    EXAM_TAKEN,
    EXAM_STANDARDISATION_DONE,
    EXAM_MARKING_DONE,
    ADMIN_CHECK_DONE,
    EXAM_MODERATION_DONE,

    // public AssessmentStatus getFirstStep(AssessmentType assessmentType) {
    // return AssessmentStatus.;
    // }

}
