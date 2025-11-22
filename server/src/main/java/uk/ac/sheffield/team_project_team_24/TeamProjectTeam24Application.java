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
      Faker faker = new Faker();

      // Populate Usr table with users
      List<User> users = new ArrayList<>();
      // Set as needed for testing
      final int NUM_USERS = 50;
      final int NUM_TEACHING_SUPPORT_TEAM = 10;
      for (int i = 0; i < NUM_USERS; i++) {
        String forename = faker.name().firstName();
        String surname = faker.name().lastName();
        String email = (forename + surname).substring(0, 6)
            + String.format("%.0f", Math.random() * 100) + "@sheffield.ac.uk";
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

      // Populate Module and ModuleStaff table
      final int NUM_MODULES = 5;
      List<User> availableStaff = userService.getUsers(UserRole.ACADEMIC_STAFF);
      final int STAFF_PER_MODULE = Math.floorDiv(availableStaff.size(), NUM_MODULES);
      List<String> startWith = Arrays.asList("Introduction to",
          "Advanced", "Analysis of");
      List<String> endWith = Arrays.asList("Software Engineering",
          "Data Science", "Algorithms");

      for (int i = 0; i < NUM_MODULES; i++) {
        String moduleCode = "COM100" + i;
        String moduleName = startWith.get(Math.floorDiv(i, startWith.size() - 1))
            + " " + endWith.get(i % (endWith.size() - 1));
        Module newModule = new Module(moduleCode, moduleName);
        moduleService.createModule(newModule);

        int offset = 0;
        if (i > 0) {
          offset = (i * STAFF_PER_MODULE) - 1;
        }
        // Assign module staff to each module
        List<ModuleStaff> moduleStaff = new ArrayList<>();
        moduleStaff.add(new ModuleStaff(newModule, availableStaff.get(offset), ModuleRole.MODULE_LEAD));
        moduleStaff.add(new ModuleStaff(newModule, availableStaff.get(offset + 1), ModuleRole.MODERATOR));
        for (int j = offset + 2; j < offset + STAFF_PER_MODULE - 2; j++) {
          moduleStaff.add(new ModuleStaff(newModule, availableStaff.get(j), ModuleRole.STAFF));
        }

        moduleStaffService.assignModuleStaff(moduleStaff);
      }

    };
  }
}
