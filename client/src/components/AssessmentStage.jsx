import React from "react";
import "../componentStyles/AssessmentStage.css";

export default function AssessmentStage({
  title,
  status, // completed/current/uncompleted
  actor, // setter/checker/moderator/exams officer/external examiner
  step,
  enableButton, // if user is the actor
  onProgress,
}) {
  return (
    <div className={`stage-card stage-${status}`}>
      <div className="stage-header">
        <h4 className="stage-title">{title}</h4>
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

      {enableButton
        ? (<button className="stage-progress-btn" onClick={onProgress}>
          Progress Stage
        </button>)
        : (<button disabled className="stage-progress-btn-disabled" onClick={onProgress}>
          Progress Stage
        </button>)
      }
    </div>
  )
}
