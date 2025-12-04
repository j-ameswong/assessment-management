package uk.ac.sheffield.team_project_team_24.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.dto.UserSignupDTO;
import uk.ac.sheffield.team_project_team_24.exception.EmptyRepositoryException;
import uk.ac.sheffield.team_project_team_24.exception.user.UsernameExistsException;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // default testing values
    // signup user
    private static final String SIGNUP_USERNAME = "testuser1";
    private static final String SIGNUP_PASSWORD = "Password1!";

    // dummy user 1
    private static final String USER1_FIRSTNAME = "testfirstname1";
    private static final String USER1_SURNAME = "testsurname1";
    private static final String USER1_EMAIL = "testuser1@test.com";
    private static final String USER1_PASSWORD = "Password2!";
    private static final UserRole USER1_ROLE = UserRole.EXAMS_OFFICER;

    // dummy user 2
    private static final String USER2_FIRSTNAME = "testfirstname1";
    private static final String USER2_SURNAME = "testsurname1";
    private static final String USER2_EMAIL = "testuser1@test.com";
    private static final String USER2_PASSWORD = "Password2!";
    private static final UserRole USER2_ROLE = UserRole.EXAMS_OFFICER;

    // create dummy users
    private static User dummyUser1() {
        return new User(USER1_FIRSTNAME, USER1_SURNAME, USER1_EMAIL, USER1_PASSWORD, USER1_ROLE);
    }

    private static User dummyUser2() {
        return new User(USER2_FIRSTNAME, USER2_SURNAME, USER2_EMAIL, USER2_PASSWORD, USER2_ROLE);
    }

    // create list of users
    private static List<User> dummyUserList() {
        return Arrays.asList(dummyUser1(), dummyUser2());
    }

    // mocks
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService classUnderTest;

    // createUser tests
    @Test
    void createUser_shouldReturnSavedUser() {
        // create user
        User savedUser = dummyUser1();
        savedUser.setId(1L);
        // define the repository behaviour
        when(userRepository.save(any())).thenReturn(savedUser);
        User result = classUnderTest.createUser(new User());
        // make sure the id of the returned User matches the one we created
        assertEquals(1L, result.getId());
        // check save was only called once
        verify(userRepository, times(1)).save(savedUser);
    }
    // no failure cases as there's no validation

    // create users tests
    @Test
    void createUsers_shouldCallSaveAllWithGivenList() {
        List<User> savedUsers = dummyUserList();
        List<User> beforeCall = new ArrayList<>(savedUsers);
        classUnderTest.createUsers(savedUsers);
        // check find all was only called once
        verify(userRepository, times(1)).saveAll(savedUsers);
        // check nothing was changed
        assertEquals(beforeCall, savedUsers);
    }
    // no failure cases as there's no validation

    // getAllUsers tests
    // success case
    @Test
    void getAllUsers_shouldReturnAllUsers_whenUsersExist() {
        List<User> savedUsers = dummyUserList();
        when(userRepository.findAll()).thenReturn(savedUsers);
        List<User> result = classUnderTest.getAllUsers();
        // output check
        assertEquals(savedUsers, result);
        // interaction check
        verify(userRepository, times(1)).findAll();
    }

    // failure case
    @Test
    void getAllUsers_shouldThrowException_whenNoUserExists() {
        EmptyRepositoryException ex =
                // check the exception is thrown
                assertThrows(EmptyRepositoryException.class, () -> classUnderTest.getAllUsers());
        // check that the error message is correct
        assertEquals("The repository UserRepository has no entries", ex.getMessage());
    }

    // get users by role tests
    @Test
    void getUsersByRole_shouldReturnSavedUsersWithGivenRole() {
        List<User> savedUsers = dummyUserList(); // both users are Exams officer
        when(userRepository.findAllByRole(UserRole.EXAMS_OFFICER)).thenReturn(savedUsers);

        List<User> result = classUnderTest.getUsers(UserRole.EXAMS_OFFICER);
        assertEquals(savedUsers, result);
        verify(userRepository, times(1)).findAllByRole(UserRole.EXAMS_OFFICER);
    }
    // no failure cases atm

    // get user (id) tests
    // success
    @Test
    void getUserById_shouldReturnSavedUser_whenUserExists() {
        User savedUser = dummyUser1();
        savedUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        User result = classUnderTest.getUser(1L);
        assertEquals(savedUser, result);
        verify(userRepository, times(1)).findById(1L);
    }

    // failure
    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> classUnderTest.getUser(1L));
    }

    // get user by email tests
    // success
    @Test
    void getUserByEmail_shouldReturnSavedUser_whenUserExists() {
        User savedUser = dummyUser1();
        when(userRepository.findByEmail(USER1_EMAIL)).thenReturn(Optional.of(savedUser));

        User result = classUnderTest.getUser(USER1_EMAIL);
        assertEquals(savedUser.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(USER1_EMAIL);
    }
    // no failure cases

    // exist user email tests
    // success
    @Test
    void existsUserByEmail_shouldReturnFalse_whenUserDoesNotExist() {
        boolean result = classUnderTest.existsUserByEmail(USER1_EMAIL);
        assertFalse(result);
        verify(userRepository, times(1)).existsUserByEmail(USER1_EMAIL);
    }

    @Test
    void existsUserByEmail_shouldReturnTrue_whenUserExists() {
        User savedUser = dummyUser1();
        when(userRepository.existsUserByEmail(USER1_EMAIL)).thenReturn(true);
        boolean result = classUnderTest.existsUserByEmail(USER1_EMAIL);
        assertTrue(result);
        verify(userRepository, times(1)).existsUserByEmail(USER1_EMAIL);
    }
    // no failure cases

    // delete user tests
    // success
    @Test
    void deleteUser_shouldCallDeleteById_whenUserExists() {
        User savedUser = dummyUser1();
        savedUser.setId(1L);
        when(userRepository.existsById(1L)).thenReturn(true);
        classUnderTest.deleteUser(1L);

        // both delete by id and exists by id were called
        verify(userRepository, times(1)).deleteById(1L);
        verify(userRepository, times(1)).existsById(1L);
    }

    // failure
    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> classUnderTest.deleteUser(1L));
    }
}
