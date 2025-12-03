package uk.ac.sheffield.team_project_team_24.generate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.github.javafaker.Faker;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentRole;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStage;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentStageLog;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentType;
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

    // Mant fields/logic have yet to be implemented
    public void generateAssessments(ModuleService moduleService,
            ModuleStaffService moduleStaffService,
            AssessmentService assessmentService,
            AssessmentStageService assessmentStageService,
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
                        new Random().nextInt(2)));
                // this will cause problems occasionally skull:
                newAssessment.setAssessmentName(m.getModuleCode()
                        + "_" + new Random().nextInt(100, 999)
                        + "_" + newAssessment.getAssessmentType().toString());
                newAssessment.setAssessmentStage(assessmentStageService.getFirstStage(
                        newAssessment.getAssessmentType()));

                assessmentService.createAssessment(newAssessment);
                AssessmentStageLog log = new AssessmentStageLog();
                log.setAssessment(newAssessment);
                log.setAssessmentStage(newAssessment.getAssessmentStage());
                log.setActedBy(newAssessment.getSetter());
                log.setNote("Created as part of test data");
                assessmentStageLogService.createAssessmentStageLog(log);
            }
        }
    }

    public void populateAssessmentStages(AssessmentStageService assessmentStageService) {
        List<StageActorDescription> cwStageNames = Arrays.asList(
                new StageActorDescription("CW_SPEC_CREATED", AssessmentRole.SETTER,
                        "Specification is created by assessment setter"),
                new StageActorDescription("CW_SPEC_CHECKED", AssessmentRole.CHECKER,
                        "Specification is checked by assessment checker"),
                // if previous step checker indicated modification required, setter does it and
                // describes the modifications here, and after done, checker can approve or reject
                new StageActorDescription("CW_SPEC_REQ_MODIFICATION", AssessmentRole.CHECKER,
                        "Checker determines if modification is required"),
                new StageActorDescription("CW_SPEC_MODIFICATION", AssessmentRole.SETTER,
                        "If required, setter makes changes and awaits checker approval"),
            // TODO:
                new StageActorDescription("CW_SPEC_RELEASED", AssessmentRole.CHECKER),
                new StageActorDescription("CW_DEADLINE_PASSED", AssessmentRole.CHECKER),
                new StageActorDescription("CW_STANDARDISATION_DONE", AssessmentRole.CHECKER),
                new StageActorDescription("CW_MARKING_DONE", AssessmentRole.CHECKER),
                new StageActorDescription("CW_MODERATION_DONE", AssessmentRole.CHECKER),
                new StageActorDescription("CW_FEEDBACK_RETURNED", AssessmentRole.CHECKER);

        for (String stageName : cwStageNames) {
            AssessmentStage newStage = new AssessmentStage();
            newStage.setAssessmentType(AssessmentType.COURSEWORK);
            newStage.setStageName(stageName);
            newStage.setStep(cwStageNames.indexOf(stageName) + 1);
            assessmentStageService.createAssessmentStage(newStage);
        }

        List<String> examStageNames = Arrays.asList(
                "EXAM_CREATED",
                "EXAM_CHECKED",
                "EXAM_MODIFIED",
                "EXAM_OFFICER_CHECKED",
                "EXTERNAL_EXAMINER_FEEDBACK",
                "SETTER_RESPONSE_SUBMITTED",
                "FINAL_EXAM_OFFICER_CHECK",
                "EXAM_TAKEN",
                "EXAM_STANDARDISATION_DONE",
                "EXAM_MARKING_DONE",
                "ADMIN_CHECK_DONE",
                "EXAM_MODERATION_DONE");

        for (String stageName : examStageNames) {
            AssessmentStage newStage = new AssessmentStage();
            newStage.setAssessmentType(AssessmentType.EXAM);
            newStage.setStageName(stageName);
            newStage.setStep(examStageNames.indexOf(stageName) + 1);
            assessmentStageService.createAssessmentStage(newStage);
        }

        List<String> testStageNames = Arrays.asList(
                "TEST_CREATED",
                "TEST_CHECKED",
                "TEST_MODIFIED",
                "TEST_TAKEN",
                "TEST_STANDARDISATION_DONE",
                "TEST_MARKING_DONE",
                "TEST_MODERATION_DONE",
                "TEST_RESULTS_RETURNED");

        for (String stageName : testStageNames) {
            AssessmentStage newStage = new AssessmentStage();
            newStage.setAssessmentType(AssessmentType.TEST);
            newStage.setStageName(stageName);
            newStage.setStep(testStageNames.indexOf(stageName) + 1);
            assessmentStageService.createAssessmentStage(newStage);
        }
    }
}
