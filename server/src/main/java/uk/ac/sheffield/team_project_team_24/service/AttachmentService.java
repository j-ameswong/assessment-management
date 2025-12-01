package uk.ac.sheffield.team_project_team_24.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AttachmentService {

    private final String uploadDir = "uploads/";

    public String saveAttachment(MultipartFile file, Long assessmentId) throws IOException {

        // Create folder
        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = "assessment_" + assessmentId + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        // Save file
        Files.write(filePath, file.getBytes());

        return filePath.toString();
    }
}

