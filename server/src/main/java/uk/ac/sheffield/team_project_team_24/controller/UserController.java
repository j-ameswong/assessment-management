package uk.ac.sheffield.team_project_team_24.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.dto.UpdatePasswordDTO;
import uk.ac.sheffield.team_project_team_24.security.CustomUserDetails;
import uk.ac.sheffield.team_project_team_24.service.UserService;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    private final UserService userService;

    // Create user
    @PostMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXAMS_OFFICER')")
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    // Get user details
    @GetMapping("/users/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    
    @PostMapping("/auth/update-password")
    public ResponseEntity<Void> updatePassword(
            Authentication authentication,
            @RequestBody UpdatePasswordDTO body) {

        Object principal = authentication.getPrincipal();
        Long userId = null;
        if (principal instanceof CustomUserDetails cud) {
            userId = cud.getId();
        }

        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        userService.updatePassword(userId, body);
        return ResponseEntity.ok().build();
    }


}
