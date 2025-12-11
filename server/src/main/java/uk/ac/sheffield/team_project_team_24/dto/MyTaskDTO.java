package uk.ac.sheffield.team_project_team_24.dto;

import java.time.Instant;

public class MyTaskDTO {
    private Long assessmentId;
    private String assessmentTitle;
    private String assessmentType;
    private String moduleCode;
    private String moduleName;
    private String currentStage;
    private String requiredRole;
    private Instant lastChangedAt;
    private String lastChangedBy;

    public MyTaskDTO() {}

    public MyTaskDTO(Long assessmentId, String assessmentTitle, String assessmentType,
                     Long moduleId, String moduleCode, String moduleName,
                     String currentStage, String requiredRole,
                     Instant lastChangedAt, String lastChangedBy) {
        this.assessmentId = assessmentId;
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.moduleId = moduleId;
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.currentStage = currentStage;
        this.requiredRole = requiredRole;
        this.lastChangedAt = lastChangedAt;
        this.lastChangedBy = lastChangedBy;
    }

    public Long getAssessmentId() {
        return assessmentId;
    }
    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }
    public String getAssessmentTitle() {
        return assessmentTitle;
    }
    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }
    public String getAssessmentType() {
        return assessmentType;
    }
    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }
    public String getModuleCode() {
        return moduleCode;
    }
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
    public String getModuleName() {
        return moduleName;
    }
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    public String getCurrentStage() {
        return currentStage;
    }
    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }
    public String getRequiredRole() {
        return requiredRole;
    }
    public void setRequiredRole(String requiredRole) {
        this.requiredRole = requiredRole;
    }
    public Instant getLastChangedAt() {
        return lastChangedAt;
    }
    public void setLastChangedAt(Instant lastChangedAt) {
        this.lastChangedAt = lastChangedAt;
    }
    public String getLastChangedBy() {
        return lastChangedBy;
    }
    public void setLastChangedBy(String lastChangedBy) {
        this.lastChangedBy = lastChangedBy;
    }

    public Long getModuleId() {
        return moduleId;
    }
    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }
    private Long moduleId;

}
