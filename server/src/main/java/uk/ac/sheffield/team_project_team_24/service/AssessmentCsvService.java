package uk.ac.sheffield.team_project_team_24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.sheffield.team_project_team_24.domain.module.Module;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.enums.AssessmentType;
import uk.ac.sheffield.team_project_team_24.repository.ModuleRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentCsvService {

    private final AssessmentService assessmentService;
    private final ModuleRepository moduleRepository;

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
                String assessmentName = fields[4].trim();
                String type = fields[5].trim();

                // module must exist already
                Module module = moduleRepository.findByModuleCode(moduleCode)
                        .orElseThrow(() -> new RuntimeException("Module not found for assessment: " + moduleCode));

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

