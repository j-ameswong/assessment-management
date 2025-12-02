package uk.ac.sheffield.team_project_team_24.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.sheffield.team_project_team_24.domain.assessment.Assessment;
import uk.ac.sheffield.team_project_team_24.domain.assessment.AssessmentType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {
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
                if (fields.length < 2) continue;

                String name = fields[0].trim();
                String type = fields[1].trim();

                Assessment a = new Assessment();
                a.setAssessmentName(name);
                a.setAssessmentType(AssessmentType.valueOf(type));

                assessments.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assessments;
    }
}
