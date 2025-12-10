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

  // get latest log for each stage
  const latestLogs = {};
  for (const log of assessmentStageLogs ?? []) {
    const id = log.assessmentStageId;

    if (!latestLogs[id]) {
      // first time seeing this stageId
      latestLogs[id] = log;
    } else {
      // keep whichever has the latest changedAt
      if (new Date(log.changedAt) > new Date(latestLogs[id].changedAt)) {
        latestLogs[id] = log;
      }
    }
  }

  const currentStep = assessmentStages?.find(
    s => s.id === assessment.assessmentStageId)?.step ?? 0;

  // add roles from all 3 levels (usr, module, assessment)
  const roles = [role, "ANY"];
  roles.push(module?.moduleStaff.find(s =>
    s.staffId === Number(localStorage.getItem("userId")))?.moduleRole);
  if (assessment?.setterId === id) { roles.push("SETTER") };
  if (assessment?.checkerId === id) { roles.push("CHECKER") };

  // group logs by stageId
  const logsByStage = {};
  for (const log of assessmentStageLogs ?? []) {
    const id = log.assessmentStageId;
    if (!logsByStage[id]) logsByStage[id] = [];
    logsByStage[id].push(log);
  }

  // sort logs oldest → newest
  for (const key in logsByStage) {
    logsByStage[key].sort((a, b) => new Date(a.changedAt) - new Date(b.changedAt));
  }

  // stages with status for AssessmentStage
  const stagesWithStatus = assessmentStages?.sort((a, b) => a.step - b.step)
    .map(stage => {
      let log = latestLogs[stage.id];
      let prevLog = latestLogs[stage.id - 1];

      // check status of current stage
      let status = "uncompleted";
      if (stage.step < currentStep) { status = "completed" }
      else if (stage.step === currentStep) { status = "current"; }

      if (log && !log.isComplete && status != "current") {
        status = "pending";
      } else if (log && log.isComplete) {
        status = "completed";
      }

      let actingStaff = null;
      let actorName = "PLACEHOLDER_NAME";
      switch (stage.actor) {
        case "SETTER":
          actingStaff = module.moduleStaff.find(s => s.staffId === assessment.setterId);
          actorName = (actingStaff.forename + " " + actingStaff.surname);
          break;
        case "CHECKER":
          actingStaff = module.moduleStaff.find(s => s.staffId === assessment.checkerId);
          actorName = (actingStaff.forename + " " + actingStaff.surname);
          break;
        case "MODERATOR":
          actingStaff = module.moduleStaff.find(s => s.moduleRole === "MODERATOR");
          actorName = (actingStaff.forename + " " + actingStaff.surname);
          break;
        case "ANY":
          actorName = ("Any staff member");
          break;
        default:
          break;
        // TODO: EXAMS_OFFICER, ADMIN, SYSTEM, EXTERNAL_EXAMINER
      }
      // determine if enable button is true based on status & user
      let enableButton = false;
      let enableReverse = false;

      // always allow exams officer/admin to advance stage
      if (status == "current") {
        if (roles.includes("ADMIN")
          || roles.includes("EXAMS_OFFICER")) {
          enableButton = true;
          if (stage.step > 1) { enableReverse = true; }
          // allow module lead to act as setter even if not setter for assessment
        } else if (stage.actor === "SETTER" && roles.includes("MODULE_LEAD")) {
          enableButton = true;
        } else if (roles.includes(stage.actor)) {
          enableButton = true;
        }
      }

      // for setter response/summary
      let summaryRequired = false;
      if (prevLog && !prevLog.isComplete) {
        summaryRequired = true;
      }

      return { ...stage, status, actorName, enableButton, log, summaryRequired, enableReverse };
    }) ?? [];

  const [furtherActionReq, setFurtherActionReq] = useState(false);
  const [note, setNote] = useState("");
  const progressStage = async (furtherActionReq, note) => {
    try {
      const payload = {
        actorId: id,
        furtherActionReq: furtherActionReq,
        note: note,
      }

      console.log("Payload sent: ", payload);

      const response = await Axios.post(`http://localhost:8080/api/assessments/${assessment.id}/advance`, payload,
        { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` } });
      console.log("Success: ", response.data);
      window.location.reload();
    } catch (err) {
      console.error(err);
      alert("Error when progressing stage");
    }
  };

  const reverseStage = async () => {
    try {
      const payload = {
        actorId: id,
        furtherActionReq: furtherActionReq,
        note: note,
      }
      const response = await Axios.post(`http://localhost:8080/api/assessments/${assessment.id}/reverse`, payload,
        { headers: { Authorization: `Bearer ${localStorage.getItem("token")}` } });
      console.log("Success: ", response.data);
      window.location.reload();
    } catch (err) {
      console.error(err);
      alert("Error when progressing stage");
    }
  };

  return (
    <>
      <div className="assessment-progress-container">
        <h2 className="assessment-progress-title">Assessment Progress</h2>

        {stagesWithStatus.map(stage => (
          <AssessmentStage
            key={stage.id}
            title={stage.description}
            status={stage.status}
            actor={stage.actor}
            actorName={stage.actorName}
            step={stage.step}
            enableButton={stage.enableButton ?? false}
            enableReverse={stage.enableReverse}
            onProgress={() => progressStage(furtherActionReq, note)}
            onReverse={() => reverseStage()}
            note={stage.log?.note ?? note}
            setNote={setNote}
            setFurtherActionReq={setFurtherActionReq}
            summaryRequired={stage.summaryRequired}
            logs={logsByStage[stage.id] || []}
          />
        ))}
      </div>
    </>
  );
}
