import React from "react";
import "../componentStyles/AssessmentStage.css";

export default function AssessmentStage({
  title,
  status, // completed/current/uncompleted
  actor, // setter/checker/moderator/exams officer/external examiner
  step,
  showButton, // if user is the actor
  onProgress,
}) {
  return (
    <div className={`stage-card stage-${status}`}>
      <div className="stage-header">
        <h3 className="stage-title">{title}</h3>
        <span className={`stage-status-label stage-status-${status}`}>
          {status.toUpperCase()}
        </span>
      </div>

      <div className="stage-info-row">
        <span className="stage-info-label">Responsible:</span>
        <span className="stage-info-value">{actor}</span>
      </div>

      <div className="stage-info-row">
        <span className="stage-info-label">Step:</span>
        <span className="stage-info-value">{step}</span>
      </div>

      {showButton && (
        <button className="stage-progress-btn" onClick={onProgress}>
          Progress Stage
        </button>
      )}
    </div>
  )
}
