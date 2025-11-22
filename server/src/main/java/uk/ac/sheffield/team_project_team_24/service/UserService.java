package uk.ac.sheffield.team_project_team_24.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;

  private static final String USER_NOT_FOUND = "User does not exist";

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void createUser(User newUser) {
    userRepository.save(newUser);
  }

  public void createUsers(List<User> newUsers) {
    userRepository.saveAll(newUsers);
  }

  public List<User> getUsers() {
    return userRepository.findAll();
  }

  public User getUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
  }

  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
    }
    userRepository.deleteById(id);
  }
}
