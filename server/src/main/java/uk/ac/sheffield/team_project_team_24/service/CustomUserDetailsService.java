package uk.ac.sheffield.team_project_team_24.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // User user = userService.getUsers()
        // .stream()
        // .filter(u -> u.getEmail().equals(email))
        // .findFirst()
        // .orElseThrow(() -> new UsernameNotFoundException("User not found: " +
        // email));

        User user = userService.getUser(email);
        return new CustomUserDetails(user);
    }
}
