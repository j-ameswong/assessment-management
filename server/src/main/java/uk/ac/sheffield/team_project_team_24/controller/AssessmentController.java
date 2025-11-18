package uk.ac.sheffield.team_project_team_24.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.sheffield.team_project_team_24.service.AssessmentService;

@RestController
public class AssessmentController {

    private final AssessmentService assessmentService;

    @Autowired
    public AssessmentController(final AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }
}
