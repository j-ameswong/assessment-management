package uk.ac.sheffield.team_project_team_24;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.github.javafaker.Faker;

import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;
import uk.ac.sheffield.team_project_team_24.service.UserService;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TeamProjectTeam24Application {

  public static void main(String[] args) {
    SpringApplication.run(TeamProjectTeam24Application.class, args);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated())
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers.frameOptions().disable());

    return http.build();
  }

  @Bean
  public CommandLineRunner commandLineRunner(UserService userService) {
    return args -> {
      Faker faker = new Faker();
      List<User> users = new ArrayList<>();
      // Set as needed for testing
      final int NUM_USERS = 50;
      final int NUM_TEACHING_SUPPORT_TEAM = 10;
      for (int i = 0; i < NUM_USERS; i++) {
        String forename = faker.name().firstName();
        String surname = faker.name().lastName();
        String email = (forename + surname).substring(0, 6) + "@sheffield.ac.uk";
        String password = faker.crypto().toString();
        UserRole role;
        if (i < NUM_TEACHING_SUPPORT_TEAM) {
          role = UserRole.TEACHING_SUPPORT_TEAM;
        } else if (i < (NUM_USERS - 1)) {
          role = UserRole.ACADEMIC_STAFF;
        } else {
          role = UserRole.EXAMS_OFFICER;
        }

        User newUser = new User(forename, surname, email, password, role);
        users.add(newUser);
      }

      userService.createUsers(users);
    };
  }
}
