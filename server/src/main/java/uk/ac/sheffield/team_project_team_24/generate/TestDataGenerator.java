package uk.ac.sheffield.team_project_team_24.generate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.github.javafaker.Faker;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentType;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;
import uk.ac.sheffield.team_project_team_24.service.AssessmentService;
import uk.ac.sheffield.team_project_team_24.service.ModuleService;
import uk.ac.sheffield.team_project_team_24.service.ModuleStaffService;
import uk.ac.sheffield.team_project_team_24.service.UserService;

public class TestDataGenerator {
    private Faker faker;
    private final int NUM_USERS;
    private final int NUM_ADMINS;
    private final int STAFF_PER_MODULE;
    private final int NUM_MODULES;

    public TestDataGenerator() {

        this.faker = new Faker();

        // default values
        this.NUM_USERS = 50;
        this.NUM_ADMINS = 10;
        this.STAFF_PER_MODULE = 4;
        this.NUM_MODULES = 5;
    }

    public TestDataGenerator(int numUsers, int numAdmins, int staffPerModule,
            int numModules)

    {
        this.faker = new Faker();
        this.NUM_USERS = numUsers;
        this.NUM_ADMINS = numAdmins;
        this.STAFF_PER_MODULE = staffPerModule;
        this.NUM_MODULES = numModules;
    }

    public void generateUsers(UserService userService) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // Populate Usr table with users
        List<User> users = new ArrayList<>();
        // Set as needed for testing
        for (int i = 0; i < NUM_USERS; i++) {
            String forename = faker.name().firstName();
            String surname = faker.name().lastName();
            String email = (forename + surname).substring(0, 6).toLowerCase()
                    + String.format("%.0f", Math.random() * 100)
                    + "@sheffield.ac.uk";
            UserRole role;
            if (i < NUM_ADMINS) {
                role = UserRole.TEACHING_SUPPORT_TEAM;
            } else if (i < (NUM_USERS - 1)) {
                role = UserRole.ACADEMIC_STAFF;
            } else {
                role = UserRole.EXAMS_OFFICER;
            }

            String rawPassword = email; // user email for password when logging in
            String hashedPassword = passwordEncoder.encode(rawPassword);

            User newUser = new User(forename, surname, email, hashedPassword, role);
            users.add(newUser);
        }

        userService.createUsers(users);
    }

    public void GenerateModules(UserService userService, ModuleService moduleService,
            ModuleStaffService moduleStaffService) {
        List<User> availableStaff = userService.getUsers(UserRole.ACADEMIC_STAFF);
        List<String> startWith = Arrays.asList("Introduction to", "Advanced", "Analysis of");
        List<String> endWith = Arrays.asList("Software Engineering", "Data Science", "Algorithms");

        // For every module, create module then generate their staff,
        // with a module lead, moderator, and the rest regular staff
        for (int i = 0; i < NUM_MODULES; i++) {
            String moduleCode = "COM100" + i;
            String moduleName = startWith.get(Math.floorDiv(i, startWith.size() - 1)) + " "
                    + endWith.get(i % (endWith.size() - 1));
            Module newModule = new Module();
            newModule.setModuleCode(moduleCode);
            newModule.setModuleName(moduleName);
            moduleService.createModule(newModule);

            int offset = i * STAFF_PER_MODULE;
            // Assign module staff to each module
            List<ModuleStaff> moduleStaff = new ArrayList<>();
            moduleStaff.add(new ModuleStaff(newModule, availableStaff.get(offset), ModuleRole.MODULE_LEAD));
            moduleStaff.add(new ModuleStaff(newModule, availableStaff.get(offset + 1), ModuleRole.MODERATOR));
            for (int j = offset + 2; j < offset + STAFF_PER_MODULE; j++) {
                moduleStaff.add(new ModuleStaff(newModule, availableStaff.get(j), ModuleRole.STAFF));
            }

            moduleStaffService.assignModuleStaff(moduleStaff);
        }
    }

    public void generateAssessments(ModuleService moduleService,
            AssessmentService assessmentService) {
        List<Module> modules = moduleService.getModules();

        for (Module m : modules) {
            Assessment newAssessment = new Assessment();
            long moduleId = m.getId();
            AssessmentType assessmentType = AssessmentType.getAllTypes().get(
                    new Random().nextInt(2));
            String assessmentName = m.getModuleCode() + "_TEST_" + moduleId;

        }
    }
}
