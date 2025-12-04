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
  if (progress) { console.log(progress) }

  return (
    <>
      <Navbar left={moduleTitle} right="Exam officer" />
    </>
  );
}
