package uk.ac.sheffield.team_project_team_24.generate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.github.javafaker.Faker;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentStages;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentType;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.domain.user.UserRole;
import uk.ac.sheffield.team_project_team_24.service.AssessmentService;
import uk.ac.sheffield.team_project_team_24.service.AssessmentStageLogService;
import uk.ac.sheffield.team_project_team_24.service.AssessmentStageService;
import uk.ac.sheffield.team_project_team_24.service.ModuleService;
import uk.ac.sheffield.team_project_team_24.service.ModuleStaffService;
import uk.ac.sheffield.team_project_team_24.service.UserService;

public class DataGenerator {
    private Faker faker;
    private final int NUM_USERS;
    private final int NUM_ADMINS;
    private final int STAFF_PER_MODULE;
    private final int NUM_MODULES;

    public DataGenerator() {

        this.faker = new Faker();

        // default values
        this.NUM_USERS = 50;
        this.NUM_ADMINS = 10;
        this.STAFF_PER_MODULE = 4;
        this.NUM_MODULES = 5;
    }

    public DataGenerator(int numUsers, int numAdmins, int staffPerModule,
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

        // system
        User admin = new User();
        admin.setEmail("admin@sheffield.ac.uk");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole(UserRole.ADMIN);
        users.add(admin);

        // Set as needed for testing
        for (int i = 0; i < NUM_USERS; i++) {
            String forename = faker.name().firstName();
            String surname = faker.name().lastName();
            String email = (forename + surname).substring(0, 6).toLowerCase()
                    + String.format("%.0f", Math.random() * 100)
                    + "@sheffield.ac.uk";
            UserRole role;
            if (i < NUM_ADMINS) {
                role = UserRole.ADMIN;
            } else if (i < (NUM_USERS - 2)) {
                role = UserRole.ACADEMIC_STAFF;
            } else if (i == (NUM_USERS - 2)) {
                role = UserRole.EXAMS_OFFICER;
            } else {
                role = UserRole.EXTERNAL_EXAMINER;
            }

            String rawPassword = email; // user email for password when logging in
            String hashedPassword = passwordEncoder.encode(rawPassword);

            User newUser = new User(forename, surname, email, hashedPassword, role);
            users.add(newUser);
        }

        userService.createUsers(users);
    }

    public void generateModules(UserService userService, ModuleService moduleService,
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
            newModule.setIsActive(true);
            moduleService.createModule(newModule);

            int offset = i * STAFF_PER_MODULE;
            // Assign module staff to cwStage module
            List<ModuleStaff> moduleStaff = new ArrayList<>();
            moduleStaff.add(new ModuleStaff(newModule, availableStaff.get(offset), ModuleRole.MODULE_LEAD));
            moduleStaff.add(new ModuleStaff(newModule, availableStaff.get(offset + 1), ModuleRole.MODERATOR));
            for (int j = offset + 2; j < offset + STAFF_PER_MODULE; j++) {
                moduleStaff.add(new ModuleStaff(newModule, availableStaff.get(j), ModuleRole.STAFF));
            }

            moduleStaffService.assignModuleStaff(moduleStaff);
        }
    }

    // Mant fields/logic have yet to be implemented
    public void generateAssessments(ModuleService moduleService,
            ModuleStaffService moduleStaffService,
            AssessmentService assessmentService,
            AssessmentStageService assessmentStageService,
            UserService userService,
            AssessmentStageLogService assessmentStageLogService) {
        List<Module> modules = moduleService.getModules();

        for (Module m : modules) {
            for (int i = 0; i < 3; i++) {
                Assessment newAssessment = new Assessment();
                newAssessment.setModule(m);
                newAssessment.setSetter(moduleStaffService.getUserByRole(m.getId(),
                        ModuleRole.MODULE_LEAD));
                newAssessment.setChecker(moduleStaffService.getUserByRole(m.getId(),
                        ModuleRole.MODERATOR));
                newAssessment.setAssessmentType(AssessmentType.getAllTypes().get(
                        new Random().nextInt(3)));
                // this will cause problems occasionally skull:
                newAssessment.setAssessmentName(m.getModuleCode()
                        + "_" + new Random().nextInt(100, 999)
                        + "_" + newAssessment.getAssessmentType().toString());
                newAssessment.setAssessmentStage(assessmentStageService.getFirstStage(
                        newAssessment.getAssessmentType()));
                newAssessment.setIsComplete(false);
                newAssessment.setDescription("This is a sample description");

                assessmentService.createAssessment(newAssessment);
                assessmentService.log(newAssessment, userService.getAdmin(), "Initialized by system", false);
            }
        }
    }

    public void populateAssessmentStages(AssessmentStageService assessmentStageService) {
        List<AssessmentStages> cwStages = Arrays.asList(
                AssessmentStages.CW_SPEC_CREATED,
                AssessmentStages.CW_SPEC_CHECKED,
                AssessmentStages.CW_SPEC_MODIFICATION,
                AssessmentStages.CW_SPEC_RELEASED,
                AssessmentStages.CW_DEADLINE_PASSED,
                AssessmentStages.CW_STANDARDISATION_DONE,
                AssessmentStages.CW_MARKING_DONE,
                AssessmentStages.CW_MODERATION_DONE,
                AssessmentStages.CW_FEEDBACK_RETURNED);

        int i = 0;
        for (AssessmentStages cwStage : cwStages) {
            AssessmentStage newStage = new AssessmentStage();
            i += 1;
            newStage.setAssessmentType(AssessmentType.COURSEWORK);
            newStage.setActor(cwStage.actor);
            newStage.setStageName(cwStage.name());
            newStage.setDescription(cwStage.description);
            newStage.setStep(i);
            assessmentStageService.createAssessmentStage(newStage);
        }

        List<AssessmentStages> examStages = Arrays.asList(
                AssessmentStages.EXAM_CREATED,
                AssessmentStages.EXAM_CHECKED,
                AssessmentStages.EXAM_MODIFICATION,
                AssessmentStages.EXAM_CHECKED_EXAMS_OFFICER,
                AssessmentStages.EXAM_MODIFICATION_EXAMS_OFFICER,
                AssessmentStages.EXAM_CHECKED_EXTERNAL_EXAMINER,
                AssessmentStages.EXAM_MODIFICATION_EXTERNAL_EXAMINER,
                AssessmentStages.EXAM_CHECKED_FINAL,
                AssessmentStages.EXAM_MODIFICATION_FINAL,
                AssessmentStages.EXAM_TAKES_PLACE,
                AssessmentStages.EXAM_STANDARDISATION_DONE,
                AssessmentStages.EXAM_MARKING_DONE,
                AssessmentStages.EXAM_MARKING_CHECKED,
                AssessmentStages.EXAM_MODERATION_DONE);

        i = 0;
        for (AssessmentStages examStage : examStages) {
            i += 1;
            AssessmentStage newStage = new AssessmentStage();
            newStage.setAssessmentType(AssessmentType.EXAM);
            newStage.setActor(examStage.actor);
            newStage.setStageName(examStage.name());
            newStage.setDescription(examStage.description);
            newStage.setStep(i);
            assessmentStageService.createAssessmentStage(newStage);
        }

        List<AssessmentStages> testStages = Arrays.asList(
                AssessmentStages.TEST_CREATED,
                AssessmentStages.TEST_CHECKED,
                AssessmentStages.TEST_MODIFICATION,
                AssessmentStages.TEST_TAKES_PLACE,
                AssessmentStages.TEST_STANDARDISATION_DONE,
                AssessmentStages.TEST_MARKING_DONE,
                AssessmentStages.TEST_MODERATION_DONE,
                AssessmentStages.TEST_RESULTS_RETURNED);

        i = 0;
        for (AssessmentStages testStage : testStages) {
            i += 1;
            AssessmentStage newStage = new AssessmentStage();
            newStage.setAssessmentType(AssessmentType.TEST);
            newStage.setActor(testStage.actor);
            newStage.setStageName(testStage.name());
            newStage.setDescription(testStage.description);
            newStage.setStep(i);
            assessmentStageService.createAssessmentStage(newStage);
        }
    }
}
