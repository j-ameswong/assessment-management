import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Axios from "axios";
import Navbar from "../components/Navbar.jsx";
import "./AssessmentOverview.css";

export default function AssessmentOverview() {
  const navigate = useNavigate();
  // example url: /modules/1/assessments/1/progress
  const moduleId = useParams().moduleId;
  const assessmentId = useParams().assessmentId;

  if (moduleId && assessmentId) { console.log(moduleId, assessmentId) };

  return (
    <>
      <Navbar left={moduleTitle} right="Exam officer" />
    </>
  );
}
