package uk.ac.sheffield.team_project_team_24;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import uk.ac.sheffield.team_project_team_24.config.RsaKeyProperties;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;
import uk.ac.sheffield.team_project_team_24.generate.DataGenerator;
import uk.ac.sheffield.team_project_team_24.service.AssessmentService;
import uk.ac.sheffield.team_project_team_24.service.AssessmentStageLogService;
import uk.ac.sheffield.team_project_team_24.service.AssessmentStageService;
import uk.ac.sheffield.team_project_team_24.service.ModuleService;
import uk.ac.sheffield.team_project_team_24.service.ModuleStaffService;
import uk.ac.sheffield.team_project_team_24.service.UserService;

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class TeamProjectTeam24Application {

    public static void main(String[] args) {
        SpringApplication.run(TeamProjectTeam24Application.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Generate fake data for testing
    @Bean
    public CommandLineRunner commandLineRunner(UserService userService,
            ModuleService moduleService,
            ModuleStaffService moduleStaffService,
            AssessmentService assessmentService,
            AssessmentStageLogService assessmentStageLogService,
            AssessmentStageService assessmentStageService,
            PasswordEncoder passwordEncoder) {
        return args -> {

            // Generate main user test for login purposes
            if (!userService.existsUserByEmail("test@sheffield.ac.uk")) {
                User testUser = new User();
                testUser.setEmail("test@sheffield.ac.uk"); // input email
                testUser.setPassword("test"); // input password
                testUser.setRole(UserRole.EXAMS_OFFICER); // can be changed for testing different roles
                testUser.setForename("Test");
                testUser.setSurname("User");

                userService.createUser(testUser);
                System.out.println("Created Test User: test@sheffield.ac.uk / test");
            }

            // All other generated users
            DataGenerator testDataGenerator = new DataGenerator();
            // create lookup table first
            testDataGenerator.populateAssessmentStages(assessmentStageService);
            testDataGenerator.generateUsers(userService);
            testDataGenerator.generateModules(userService, moduleService, moduleStaffService);
            testDataGenerator.generateAssessments(moduleService,
                    moduleStaffService,
                    assessmentService,
                    assessmentStageService,
                    userService,
                    assessmentStageLogService);

        };

    }
}
