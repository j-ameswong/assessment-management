import React from "react";
import { useState } from "react";
import "../componentStyles/AssessmentStage.css";

export default function AssessmentStage({
  title,
  status, // completed/current/uncompleted
  actor, // setter/checker/moderator/exams officer/external examiner
  step,
  enableButton, // if user is the actor
  onProgress,
  setFurtherActionReq,
  note,
  setNote,
  summaryRequired,
}) {

  const [isChecked, setIsChecked] = useState(false);

  const onCheckHandler = () => {
    setIsChecked(!isChecked);
    setFurtherActionReq(!isChecked);
  }

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

      {["CHECKER", "EXAMS_OFFICER", "EXTERNAL_EXAMINER"].includes(actor) &&
        (<div className="stage-info-row">
          <span className="stage-info-label">Request Follow-up</span>
          <input disabled={!enableButton} checked={isChecked} onChange={onCheckHandler} type="checkbox" />
        </div>)}

      {(actor === "SETTER") && summaryRequired && enableButton && (
        <div className="stage-info-row">
          <span className="stage-info-label">Response:</span>
          <textarea
            onChange={(t) => setNote(t.target.value)}
            content={note}
            className="stage-textarea">
          </textarea>
        </div>
      )}

      {isChecked || (actor === "EXTERNAL_EXAMINER" && enableButton) && (
        <div className="stage-info-row">
          <span className="stage-info-label">Feedback:</span>
          <textarea
            onChange={(t) => setNote(t.target.value)}
            content={note}
            className="stage-textarea">
          </textarea>
        </div>
      )}

      {(status === "pending") && (
        <div className="stage-info-row">
          <span className="stage-info-label">Feedback:</span>
          <p>{note}</p>
        </div>
      )}

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
