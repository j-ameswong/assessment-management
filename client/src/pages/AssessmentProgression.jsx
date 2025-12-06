import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Axios from "axios";
import Navbar from "../components/Navbar.jsx";
import "./AssessmentProgression.css";
import AssessmentStage from "../components/AssessmentStage.jsx";

export default function AssessmentProgression() {
  // redirect to login if no token
  const navigate = useNavigate();
  useEffect(() => {
    if (!localStorage.getItem("token")) { navigate("/") };
  }, [navigate]);

  const id = Number(localStorage.getItem("userId"));
  const role = (localStorage.getItem("role"));

  // example url: /modules/:moduleId/assessments/:assessmentId/progress
  const assessmentId = useParams().assessmentId;
  const [progress, setProgress] = useState([]);

  // fetch req info from api
  useEffect(() => {
    if (!assessmentId) { return };
    Axios.get(
      `http://localhost:8080/api/assessments/${assessmentId}/progress`,
      { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` } })
      .then(({ data }) => setProgress(data))
  }, [assessmentId]);
  if (!progress) return <p>Loading...</p>;

  // extract data from dto
  const { module, assessment, assessmentStages, assessmentStageLogs } = progress;

  // always ensure placeholder if api missing data
  const moduleTitle = module
    ? `${progress.module.moduleCode} ${progress?.module.moduleName}`
    : "COMXXXX UNKNOWN";

  // get info from logs about past stages
  const completedLogs = assessmentStageLogs?.filter(log => log.isComplete)
    .sort((a, b) => new Date(a.changedAt) - new Date(b.changedAt)) ?? [];

  const lastCompletedStageId = completedLogs.at(-1)?.assessmentStageId ?? null;
  const lastCompletedStage = assessmentStages?.find(s => s.id === lastCompletedStageId) ?? {};
  const lastCompletedStep = lastCompletedStage?.step ?? 0;

  // add roles from all 3 levels (usr, module, assessment)
  const roles = [role, "ANY"];
  roles.push(module?.moduleStaff.find(s =>
    s.staffId === Number(localStorage.getItem("userId")))?.moduleRole);
  if (assessment?.setterId === id) { roles.push("SETTER") };
  if (assessment?.checkerId === id) { roles.push("CHECKER") };

  const stagesWithStatus = assessmentStages?.sort((a, b) => a.step - b.step)
    .map(stage => {
      // check status of current stage
      let status = "uncompleted";
      if (stage.step <= lastCompletedStep) { status = "completed" }
      else if (stage.step === lastCompletedStep + 1) { status = "current"; }

      // determine if enable button is true based on status & user
      let enableButton = false;

      if (status == "current") {
        if (roles.includes("ADMIN")
          || roles.includes("EXAMS_OFFICER")) {
          enableButton = true;
        } else if (roles.includes(stage.actor)) {
          enableButton = true;
        }
      }

      return { ...stage, status, enableButton };
    }) ?? [];

  const progressStage = async (furtherActionReq, note) => {
    try {
      const payload = {
        furtherActionReq: furtherActionReq,
        note: note,
      }

      console.log("Payload sent: ", payload);

      const response = await Axios.post(`http://localhost:8080/api/assessments/${assessment.id}/advance`,
        { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` } }, payload);
      console.log("Success: ", response.data);
      window.location.reload();
    } catch (err) {
      console.error(err);
      alert("Error when progressing stage");
    }
  };

  return (
    <>
      <Navbar left={moduleTitle} right="Exam officer" />

      <div className="assessment-progress-container">
        <h2 className="assessment-progress-title">Assessment Progress</h2>

        {stagesWithStatus.map(stage => (
          <AssessmentStage
            key={stage.id}
            title={stage.description}
            status={stage.status}
            actor={stage.actor}
            step={stage.step}
            enableButton={stage.enableButton ?? false}
            onProgress={() => progressStage(false, "TODO")}
          />
        ))}
      </div>
    </>
  );
}
