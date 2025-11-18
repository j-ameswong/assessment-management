package uk.ac.sheffield.team_project_team_24.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.sheffield.team_project_team_24.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }
}
