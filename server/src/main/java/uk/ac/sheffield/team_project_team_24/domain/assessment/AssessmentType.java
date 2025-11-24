package uk.ac.sheffield.team_project_team_24.domain.assessment;

import java.util.Arrays;
import java.util.List;

public enum AssessmentType {
    COURSEWORK, // cw
    TEST, // in-semester test
    EXAM; // final exam

    private AssessmentType() {
    }

    public static List<AssessmentType> getAllTypes() {
        return Arrays.asList(AssessmentType.COURSEWORK,
                AssessmentType.TEST,
                AssessmentType.EXAM);
    }
}
