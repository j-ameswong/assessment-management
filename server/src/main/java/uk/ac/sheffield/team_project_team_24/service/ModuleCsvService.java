package uk.ac.sheffield.team_project_team_24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleRole;
import uk.ac.sheffield.team_project_team_24.domain.module.ModuleStaff;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.dto.CreateModuleDTO;
import uk.ac.sheffield.team_project_team_24.repository.ModuleStaffRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ModuleCsvService {

    private final UserService userService;
    private final ModuleService moduleService;
    private final ModuleStaffRepository moduleStaffRepository;

    public List<Module> parse(MultipartFile file) {
        List<Module> modules = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirst = true;

            while ((line = br.readLine()) != null) {

                if (isFirst) {
                    isFirst = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length < 4)
                    continue;

                String moduleCode = fields[0].trim();
                String moduleName = fields[1].trim();
                String leadFullName = fields[2].trim();
                String staffList = fields[3].trim();

                // find module lead
                User moduleLead = userService.findByFullName(leadFullName);

                // staff list
                List<User> staffIds = new ArrayList<>();
                Set<Long> staffIdSet = new HashSet<>();

                if (!staffList.isBlank()) {
                    String[] staffNames = staffList.split(";");

                    for (String s : staffNames) {
                        String trimmed = s.trim();
                        if (trimmed.isEmpty()) continue;

                        User u = userService.findByFullName(trimmed);

                        if (!u.getId().equals(moduleLead.getId()) && staffIdSet.add(u.getId())) {
                            staffIds.add(u);
                        }
                    }
                }

                // create module
                CreateModuleDTO dto = new CreateModuleDTO(
                        moduleCode,
                        moduleName,
                        null,
                        null,
                        new ArrayList<>()
                );

                Module module = moduleService.createModule(dto);
                modules.add(module);

                // module lead entry
                moduleStaffRepository.save(
                        new ModuleStaff(module, moduleLead, ModuleRole.MODULE_LEAD)
                );

                // create staff list
                for (User u : staffIds) {
                    moduleStaffRepository.save(
                            new ModuleStaff(module, u, ModuleRole.STAFF)
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Cannot processing CSV: " + e.getMessage(), e);
        }

        return modules;
    }
}
