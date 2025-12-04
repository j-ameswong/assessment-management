import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Axios from "axios";
import Navbar from "../components/Navbar.jsx";
import "./AssessmentOverview.css";

export default function AssessmentOverview() {
  const navigate = useNavigate();
  // example url: /modules/1/assessments/1/progress
  const assessmentId = useParams().assessmentId;
  const [progress, setProgress] = useState([]);

  useEffect(() => {
    if (!assessmentId) { return };
    Axios.get(`http://localhost:8080/api/assessments/${assessmentId}/progress`)
      .then(({ data }) => setProgress(data))
  }, [assessmentId]);

  const moduleTitle = progress?.module
    ? progress.module.moduleCode + " " + progress?.module.moduleName
    : "COMXXXX UNKNOWN"

  const currentStage = progress?.assessmentStageId ?? 0;
  const stages = progress?.assessmentStages ?? [];
  const pastStages = progress?.assessmentStageLogs ?? [];
  const completedStages = pastStages?.filter(s => s.isComplete) ?? [];
  const lastCompletedStep = stages?.find(s => s.id === (
    completedStages?.at(-1).assessmentStageId ?? 0))?.step ?? 0;

  let showPastStages = pastStages.map(ps => ({
    key: `past-${ps.id}`,
    assessmentStageId: ps.assessmentStageId,
    name: stages.find(i => i.id === ps.assessmentStageId)?.description ?? "DESC_NOT_FOUND"
  }))

  let showStages = stages.filter(s =>
    s.step > lastCompletedStep).map(stage => ({
      key: stage.id,
      assessmentStageId: stage.id,
      name: stage?.description ?? "DESC_NOT_FOUND"
    }));
  if (completedStages) { console.log(completedStages.at(-1)) };
  if (lastCompletedStep) { console.log(lastCompletedStep) };
  if (showStages) { console.log(showStages) };

  return (
    <>
      <Navbar left={moduleTitle} right="Exam officer" />
      <div>
        {showPastStages.map(s => (
          <p key={s.key} style={{ color: 'green' }}>{s.name}</p>))}
      </div>
      <div>
        {showStages.map(t => (
          <p key={t.key} style={{ color: 'red' }}>{t.name}</p>))}
      </div>
    </>
  );
}
