package uk.ac.sheffield.team_project_team_24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentType;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.dto.CreateModuleDTO;
import uk.ac.sheffield.team_project_team_24.repository.ModuleStaffRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CsvService {
    private final UserService userService;
    private final ModuleService moduleService;
    private final AssessmentService assessmentService;
    private final ModuleStaffRepository moduleStaffRepository;

    public List<Assessment> parse(MultipartFile file) {
        List<Assessment> assessments = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirst = true;

            while ((line = br.readLine()) != null) {

                if (isFirst) {
                    isFirst = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length < 6)
                    continue;

                String moduleCode = fields[0].trim();
                String moduleName = fields[1].trim();
                String leadFullName = fields[2].trim();
                String staffList = fields[3].trim();
                String assessmentName = fields[4].trim();
                String type = fields[5].trim();

                // find the module lead
                User moduleLead = userService.findByFullName(leadFullName);

                // staff list
                List<User> staffIds = new ArrayList<>();
                Set<Long> staffIdSet = new HashSet<>();

                if (!staffList.isBlank()) {
                    String[] staffNames = staffList.split(";");
                    for (String s : staffNames) {
                        String trimmed = s.trim();
                        if (trimmed.isEmpty()) {
                            continue;
                        }

                        User u = userService.findByFullName(trimmed);

                        if (u.getId().equals(moduleLead.getId())) {
                            continue;
                        }

                        if (staffIdSet.add(u.getId())) {
                            staffIds.add(u);
                        }
                    }
                }
                // Create Module
                CreateModuleDTO dto = new CreateModuleDTO(
                        moduleCode,
                        moduleName,
                        null,
                        null,
                        new ArrayList<>()
                );

                Module module = moduleService.createModule(dto);

                // Create Module Lead
                moduleStaffRepository.save(
                        new ModuleStaff(module, moduleLead, ModuleRole.MODULE_LEAD)
                );
                System.out.println("LEAD: " + moduleLead.getForename() + " " + moduleLead.getSurname());


                // create staff list
                for (User u : staffIds) {
                    moduleStaffRepository.save(
                            new ModuleStaff(module, u, ModuleRole.STAFF)
                    );
                    System.out.println("STAFF: " + u.getForename() + " " + u.getSurname());
                }

                Assessment a = new Assessment();
                a.setAssessmentName(assessmentName);
                a.setAssessmentType(AssessmentType.valueOf(type));
                a.setModule(module);

                assessments.add(a);
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot processing CSV: " + e.getMessage(), e);
        }

        assessmentService.saveAll(assessments);
        return assessments;
    }

}
