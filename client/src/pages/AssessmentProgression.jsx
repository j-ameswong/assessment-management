import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Axios from "axios";
import Navbar from "../components/Navbar.jsx";
import "./AssessmentProgression.css";

export default function AssessmentProgression() {
  const navigate = useNavigate();
  // example url: /modules/:moduleId/assessments/:assessmentId/progress
  const assessmentId = useParams().assessmentId;
  const [progress, setProgress] = useState([]);

  useEffect(() => {
    if (!assessmentId) { return };
    Axios.get(`http://localhost:8080/api/assessments/${assessmentId}/progress`)
      .then(({ data }) => setProgress(data))
  }, [assessmentId]);

  if (!progress) return <p>Loading...</p>;

  const { module, assessmentStages, assessmentStageLogs } = progress;

  const moduleTitle = module
    ? `${progress.module.moduleCode} ${progress?.module.moduleName}`
    : "COMXXXX UNKNOWN";

  const completedLogs = assessmentStageLogs?.filter(log => log.isComplete)
    .sort((a, b) => new Date(a.changedAt) - new Date(b.changedAt)) ?? [];

  const lastCompletedStageId = completedLogs.at(-1)?.assessmentStageId ?? null;
  const lastCompletedStage = assessmentStages?.find(s => s.id === lastCompletedStageId) ?? {};
  const lastCompletedStep = lastCompletedStage?.step ?? 0;

  const stagesWithStatus = assessmentStages?.sort((a, b) => a.step - b.step)
    .map(stage => {
      let status = "uncompleted";

      if (stage.step <= lastCompletedStep) {
        status = "completed";
      }
      else if (stage.step === lastCompletedStep + 1) {
        status = "current";
      }

      return { ...stage, status };
    }) ?? [];

  return (
    <>
      <Navbar left={moduleTitle} right="Exam officer" />

      <div>
        <h2>Assessment Progress</h2>

        <div>
          {stagesWithStatus.map(stage => (
            <p key={stage.id} className={stage.status}>
              {stage.description}
            </p>
          ))}
        </div>
      </div>
    </>
  );
}
