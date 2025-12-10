package uk.ac.sheffield.team_project_team_24.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;
import uk.ac.sheffield.team_project_team_24.dto.FirstTimeLoginDTO;
import uk.ac.sheffield.team_project_team_24.dto.LoginDTO;
import uk.ac.sheffield.team_project_team_24.dto.TokenDTO;
import uk.ac.sheffield.team_project_team_24.dto.UserSignupDTO;
import uk.ac.sheffield.team_project_team_24.exception.EmptyRepositoryException;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;
import uk.ac.sheffield.team_project_team_24.security.CustomUserDetails;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    private static final String USER_NOT_FOUND = "User does not exist";

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User createUser(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    public void createUsers(List<User> newUsers) {
        userRepository.saveAll(newUsers);
    }

    public TokenDTO signupNewUser(UserSignupDTO userSignupDTO) {
        User newUser = UserSignupDTO.toEntity(userSignupDTO);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        userRepository.save(newUser);
        CustomUserDetails securityUser = (CustomUserDetails) userDetailsService.loadUserByUsername(newUser.getEmail());
        return tokenService.generateToken(securityUser.getAuthorities(), newUser.getEmail());
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new EmptyRepositoryException("UserRepository");
        } else {
            return users;
        }
    }

    public FirstTimeLoginDTO onLogin(LoginDTO loginDTO, TokenDTO tokenDTO) {
        String username = loginDTO.getUsername();
        User user = getUser(username);

        return new FirstTimeLoginDTO(
                user.getId(),
                user.getRole(),
                tokenDTO.getToken());
    }

    public List<User> getUsers(UserRole userRole) {
        return userRepository.findAllByRole(userRole);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
    }

    public User getAdmin() {
        return getUser("admin@sheffield.ac.uk");
    }

    public User getExamsOfficer() {
        return getUsers(UserRole.EXAMS_OFFICER).get(0);
    }

    public User updateUserRole(Long id, UserRole newRole) {
        User user = getUser(id);
        user.setRole(newRole);
        return userRepository.save(user);
    }

    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }


    public void updatePassword(Long userId, uk.ac.sheffield.team_project_team_24.dto.UpdatePasswordDTO body) {
        if (body == null
                || body.getOldPassword() == null
                || body.getNewPassword() == null
                || body.getConfirmNewPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing fields");
        }

        if (!Objects.equals(body.getNewPassword(), body.getConfirmNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        }

        Optional<uk.ac.sheffield.team_project_team_24.domain.user.User> opt = userRepository.findById(userId);
        uk.ac.sheffield.team_project_team_24.domain.user.User user = opt.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(body.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Old password incorrect");
        }

        user.setPassword(passwordEncoder.encode(body.getNewPassword()));
        userRepository.save(user);
    }

    
}
