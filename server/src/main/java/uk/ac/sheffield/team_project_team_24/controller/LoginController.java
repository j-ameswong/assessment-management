package uk.ac.sheffield.team_project_team_24.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.dto.FirstTimeLoginDTO;
import uk.ac.sheffield.team_project_team_24.dto.LoginDTO;
import uk.ac.sheffield.team_project_team_24.dto.TokenDTO;
import uk.ac.sheffield.team_project_team_24.dto.UserSignupDTO;
import uk.ac.sheffield.team_project_team_24.security.CustomUserDetails;
import uk.ac.sheffield.team_project_team_24.service.TokenService;
import uk.ac.sheffield.team_project_team_24.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // only admins should be allowed to signup users
    @PostMapping("/signup")
    public ResponseEntity<TokenDTO> signup(@RequestBody UserSignupDTO userSignupDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.signupNewUser(userSignupDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<FirstTimeLoginDTO> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(authentication.getAuthorities());

        TokenDTO tokenDTO = tokenService.generateToken(authentication.getAuthorities(), authentication.getName());
        FirstTimeLoginDTO dto = userService.onLogin(loginDTO, tokenDTO);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.getUserByEmail(userDetails.getUsername());

        return ResponseEntity.ok(user);
    }


}