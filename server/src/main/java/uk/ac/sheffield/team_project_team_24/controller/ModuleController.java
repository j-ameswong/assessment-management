package uk.ac.sheffield.team_project_team_24.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.sheffield.team_project_team_24.service.ModuleService;

@RestController
public class ModuleController {

    private final ModuleService moduleService;

    @Autowired
    public ModuleController(final ModuleService assessmentService) {
        this.moduleService = assessmentService;
    }
}
