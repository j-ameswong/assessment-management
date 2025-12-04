package uk.ac.sheffield.team_project_team_24.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;
import uk.ac.sheffield.team_project_team_24.exception.EmptyRepositoryException;
import uk.ac.sheffield.team_project_team_24.exception.user.UserNotFoundException;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;

@Service
@Transactional
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    private static final String USER_NOT_FOUND = "User does not exist";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User newUser) {
        return userRepository.save(newUser);
    }

    public void createUsers(List<User> newUsers) {
        userRepository.saveAll(newUsers);
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
