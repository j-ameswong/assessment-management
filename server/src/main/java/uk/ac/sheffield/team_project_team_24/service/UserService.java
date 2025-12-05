package uk.ac.sheffield.team_project_team_24.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;
import uk.ac.sheffield.team_project_team_24.dto.TokenDTO;
import uk.ac.sheffield.team_project_team_24.dto.UserSignupDTO;
import uk.ac.sheffield.team_project_team_24.exception.EmptyRepositoryException;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;
import uk.ac.sheffield.team_project_team_24.security.CustomUserDetails;

@Service
@Transactional
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    private static final String USER_NOT_FOUND = "User does not exist";

    public UserService(UserRepository userRepository,
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            TokenService tokenService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User createUser(User newUser) {
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

    public List<User> getUsers(UserRole userRole) {
        return userRepository.findAllByRole(userRole);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
    }

    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
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
}
