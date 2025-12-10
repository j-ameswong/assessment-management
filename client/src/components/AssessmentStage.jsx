import React from "react";
import { useState } from "react";
import "./AssessmentStage.css";

export default function AssessmentStage({
  title,
  status, // completed/current/uncompleted
  actor, // setter/checker/moderator/exams officer/external examiner
  actorName,
  step,
  enableButton, // if user is the actor
  enableReverse,
  onProgress,
  onReverse,
  setFurtherActionReq,
  note,
  setNote,
  summaryRequired,
  logs,
  moduleStaff
}) {

  const [isChecked, setIsChecked] = useState(false);

  const [expanded, setExpanded] = useState(false);

  const toggleExpanded = () => setExpanded(e => !e);

  const onCheckHandler = () => {
    setIsChecked(!isChecked);
    setFurtherActionReq(!isChecked);
  }

  const toCapitalize = (str) => {
    let newStr = str.replaceAll("_", " ");
    newStr = newStr[0] + newStr.slice(1).toLowerCase();

    return newStr;

  }

  const getActorName = (id) => {
    const staff = moduleStaff.find(s => s.staffId === id);
    if (!staff) return "Unknown";
    return `${staff.forename} ${staff.surname}`;
  };

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
        <span className="stage-info-value">{toCapitalize(actor) + (
          !(actor === "ADMIN" || actor === "SYSTEM") ? ` (${actorName})` : ""
        )}</span>
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

      {(isChecked || (actor === "EXTERNAL_EXAMINER" && enableButton)) && (
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

      {enableReverse
        ? (<button className="stage-reverse-btn" onClick={onReverse}>
          Reverse Stage
        </button>)
        : (<button disabled className="stage-reverse-btn-disabled" onClick={onReverse}>
          Reverse Stage
        </button>)
      }

      {/* expand button */}
      {(logs.length > 0) && (
        <div className="stage-expand-toggle" onClick={toggleExpanded}>
          {expanded ? "▲ Hide Logs" : "▼ Show Logs"}
        </div>
      )}

      {expanded && (logs.length > 0) && (
        <div className="stage-logs-container">
          {logs.length === 0 ? (
            <p className="no-logs">No logs for this stage.</p>
          ) : (
            logs.map(log => (
              <div key={log.id} className="stage-log-entry">
                <p><strong>Actor:</strong> {getActorName(log.actedById)}</p>
                <p><strong>Time:</strong> {new Date(log.changedAt).toLocaleString()}</p>
                <p><strong>Status:</strong> {log.isComplete ? "Complete" : "Pending"}</p>
                {log.note && <p><strong>Note:</strong> {log.note}</p>}
                <hr />
              </div>
            ))
          )}
        </div>
      )}
    </div>
  )
}
