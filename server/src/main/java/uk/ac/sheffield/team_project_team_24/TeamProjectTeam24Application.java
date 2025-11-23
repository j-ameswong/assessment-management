package uk.ac.sheffield.team_project_team_24;

import java.util.ArrayList;
import java.util.Arrays;
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
import uk.ac.sheffield.team_project_team_24.generate.TestDataGenerator;
import uk.ac.sheffield.team_project_team_24.service.ModuleService;
import uk.ac.sheffield.team_project_team_24.service.ModuleStaffService;
import uk.ac.sheffield.team_project_team_24.service.UserService;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TeamProjectTeam24Application {

  public static void main(String[] args) {
    SpringApplication.run(TeamProjectTeam24Application.class, args);
  }

  // Bean to disable Spring Security for legacy h2-console access
  @SuppressWarnings("removal")
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

  // Generate fake data for testing
  @Bean
  public CommandLineRunner commandLineRunner(UserService userService, ModuleService moduleService,
      ModuleStaffService moduleStaffService) {
    return args -> {
      TestDataGenerator testDataGenerator = new TestDataGenerator();
      testDataGenerator.generateUsers(userService);
      testDataGenerator.GenerateModules(userService, moduleService, moduleStaffService);
    };
  }
}
