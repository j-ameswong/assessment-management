package uk.ac.sheffield.team_project_team_24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;
import uk.ac.sheffield.team_project_team_24.dto.UserCreationDTO;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String rawPassword = loginData.get("password");

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found");
        }

        if (!passwordEncoder.matches(rawPassword, user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Wrong password");
        }

        UserCreationDTO userDTO = new UserCreationDTO();
        userDTO.setEmail(user.get().getEmail());
        userDTO.setForename(user.get().getForename());
        userDTO.setRole(user.get().getRole());
        userDTO.setId(user.get().getId());

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/test")
    public String test() {
        return "Backend OK";
    }

}

