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

  const stages = progress?.assessmentStages ?? [];
  const pastStages = progress?.assessmentStageLogs ?? [];
  const completedStages = progress?.assessmentStageLogs?.filter(s => s.isComplete) ?? [];
  let showPastStages = pastStages.map(ps => ({
    key: `past${ps.id}`,
    name: stages.find(i => i.id === ps.assessmentStageId)?.description ?? "DESC_NOT_FOUND"
  }))
  let showStages = stages.filter(s =>
    s.step > 1).map(stage => ({
      key: `future-${stage.id}`,
      name: stage?.description ?? "DESC_NOT_FOUND"
    }));

  if (completedStages) { console.log(completedStages) };

  return (
    <>
      <Navbar left={moduleTitle} right="Exam officer" />
      <div>
        {showPastStages.map(t => (
          <p>{t.name}</p>))}
      </div>
      <div>
        {showStages.map(t => (
          <p>{t.name}</p>))}
      </div>
    </>
  );
}
