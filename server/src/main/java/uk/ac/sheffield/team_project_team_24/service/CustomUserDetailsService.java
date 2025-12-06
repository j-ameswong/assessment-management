package uk.ac.sheffield.team_project_team_24.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;
import uk.ac.sheffield.team_project_team_24.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found"));

        return new CustomUserDetails(user);
    }
}
