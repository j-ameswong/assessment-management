import React from "react";
import "./AssessmentInfo.css";

export default function AssessmentInfo({ assessment, module, currentStage, goBack }) {
  return (
    <div className="assessment-info-card">
      <button className="back-btn" onClick={() => goBack()}>
        <span className="arrow">←</span>
        <span>Back</span>
      </button>
      <hr />
      <h3 className="assessment-info-title">Assessment Information</h3>

      <div className="assessment-info-grid">
        <div className="info-item">
          <span className="info-label">Assessment Name:</span>
          <span className="info-value">{assessment?.name}</span>
        </div>

        <div className="info-item">
          <span className="info-label">Type:</span>
          <span className="info-value">{assessment?.type}</span>
        </div>

        <div className="info-item">
          <span className="info-label">Module:</span>
          <span className="info-value">
            {module?.moduleCode} — {module?.moduleName}
          </span>
        </div>

        <div className="info-item">
          <span className="info-label">Current Stage:</span>
          <span className="info-value">{`(${currentStage?.step}) ${currentStage?.description ?? "—"}`}</span>
        </div>

        <div className="info-item">
          <span className="info-label">Completion Status:</span>
          <span className="info-value">
            {assessment?.isComplete ? "Completed" : "In Progress"}
          </span>
        </div>

        <div className="info-item info-wide">
          <span className="info-label">Description:</span>
          <span className="info-value">{assessment?.description}</span>
        </div>

        <div className="info-item">
          <span className="info-label">Setter:</span>
          <span className="info-value">
            {assessment?.setterId
              ? `${module.moduleStaff.find(s => s.staffId === assessment.setterId)?.forename}
                 ${module.moduleStaff.find(s => s.staffId === assessment.setterId)?.surname}`
              : "—"}
          </span>
        </div>

        <div className="info-item">
          <span className="info-label">Checker:</span>
          <span className="info-value">
            {assessment?.checkerId
              ? `${module.moduleStaff.find(s => s.staffId === assessment.checkerId)?.forename}
                 ${module.moduleStaff.find(s => s.staffId === assessment.checkerId)?.surname}`
              : "—"}
          </span>
        </div>

        <div className="info-item">
          <span className="info-label">External Examiner:</span>
          <span className="info-value">
            {assessment?.externalExaminer
              ? `${assessment.externalExaminer.forename} ${assessment.externalExaminer.surname}`
              : "—"}
          </span>
        </div>
      </div>
    </div>
  );
}

