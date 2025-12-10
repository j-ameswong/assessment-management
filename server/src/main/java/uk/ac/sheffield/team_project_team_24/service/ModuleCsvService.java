package uk.ac.sheffield.team_project_team_24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import uk.ac.sheffield.team_project_team_24.domain.user.User;
import uk.ac.sheffield.team_project_team_24.dto.CreateModuleDTO;
import uk.ac.sheffield.team_project_team_24.dto.ModuleDTO;
import uk.ac.sheffield.team_project_team_24.repository.UserRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ModuleCsvService {

    private final ModuleService moduleService;
    private final UserRepository userRepository;

    public List<ModuleDTO> processCsv(MultipartFile file) {
        List<ModuleDTO> createdModules = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirst = true;

            while ((line = br.readLine()) != null) {

                if (isFirst) {
                    isFirst = false;
                    continue;
                }

                String[] fields = line.split(",", -1);
                if (fields.length < 3)
                    continue;

                String moduleCode = fields[0].trim();
                String moduleName = fields[1].trim();
                String leadFullName = fields[2].trim();
                String staffList = fields.length >= 4 ? fields[3].trim() : "";

                // Reading module lead name
                String[] leadFullNameParts = leadFullName.split(" ", 2);
                if (leadFullNameParts.length < 2) {
                    System.err.println("Full name not found:  " + leadFullName);
                    continue;
                }
                String leadForename = leadFullNameParts[0];
                String leadSurname = leadFullNameParts[1];
                User leadUser = userRepository.findByForenameAndSurname(leadForename, leadSurname)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Lead full name not found: " + leadFullName)
                        );

                // Reading other staff members
                List<Long> staffIds = new ArrayList<>();
                if (!staffList.isBlank()) {
                    String[] staffNames = staffList.split(",");
                    for (String staffFullName : staffNames) {
                        String trimmed = staffFullName.trim();
                        String[] parts = trimmed.split(" ", 2);

                        if (parts.length < 2)
                            continue;

                        userRepository.findByForenameAndSurname(parts[0], parts[1])
                                .ifPresentOrElse(
                                        user -> staffIds.add(user.getId()),
                                        () -> System.err.println("Staff not found: " + trimmed)
                                );
                    }
                }

                CreateModuleDTO dto = new CreateModuleDTO();
                dto.setModuleCode(moduleCode);
                dto.setModuleName(moduleName);
                dto.setModuleLeadId(leadUser.getId());
                dto.setModuleModeratorId(null);
                dto.setStaffIds(staffIds);

                createdModules.add(ModuleDTO.fromEntity(
                        moduleService.createModule(dto)
                ));
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Error processing .csv: " + e.getMessage());
        }

        return createdModules;
    }
}