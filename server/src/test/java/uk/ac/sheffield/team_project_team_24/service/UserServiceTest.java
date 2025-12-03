package uk.ac.sheffield.team_project_team_24.service;

import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

//    @Mock
//    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService classUnderTest;

    // default testing values
    // signup user
    private static final String SIGNUP_USERNAME = "testuser1";
    private static final String SIGNUP_PASSWORD = "Password1!";

    // dummy user
    private static final String USER_FIRSTNAME = "testfirstname";
    private static final String USER_SURNAME = "testsurname";
    private static final String USER_EMAIL = "testuser@test.com";
    private static final String USER_PASSWORD = "Password2!";
    private static final UserRole USER_ROLE = UserRole.EXAMS_OFFICER;


    // don't think this is fully testable at the moment
//    @Test
//    void signUpNewUser_shouldThrowExceptionWhenUsernameAlreadyExists() {
//        UserSignupDTO userSignupDTO = new UserSignupDTO();
//        userSignupDTO.setUsername(SIGNUP_USERNAME);
//        userSignupDTO.setPassword(SIGNUP_PASSWORD);
//        when(userRepository.existsUserByUsername(userSignupDTO.getUsername()))
//                .thenReturn(true);
//        assertThrows(UsernameExistsException.class, () -> {
//            classUnderTest.createUser(userSignupDTO);
//        });
//    }

    @Test
    void getAllUsers_shouldThrowException_whenNoUserExists() {
        EmptyRepositoryException ex =
        // check the exception is thrown
            assertThrows(EmptyRepositoryException.class, () -> classUnderTest.getAllUsers());
        // check that the error message is correct
        assertEquals("The repository UserRepository has no entries", ex.getMessage());
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers_whenUsersExist() {
        User savedUser = new User(USER_FIRSTNAME, USER_SURNAME, USER_EMAIL, USER_PASSWORD, USER_ROLE);
        savedUser.setId(1L);


    }

}