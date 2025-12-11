import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Axios from "axios";
import Navbar from "../components/Navbar.jsx";
import "./AssessmentOverview.css";

export default function AssessmentOverview() {
  const navigate = useNavigate();
  // example url: /modules/assessments/1
  const moduleId = useParams().moduleId;

  // fetch data from api
  const [overview, setOverview] = useState(null);
  useEffect(() => {
    const fetchOverview = async () => {
      try {
        const response = await Axios.get(
          `http://localhost:8080/api/modules/${moduleId}/overview`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        setOverview(response.data);
      } catch (error) {
        console.error("Failed to fetch overview:", error);

        if (error.response) {
          // Server responded with an error code
          console.error("Status:", error.response.status);
          console.error("Data:", error.response.data);
        } else if (error.request) {
          // No response received
          console.error("No response from server");
        } else {
          // Something else happened
          console.error("Error:", error.message);
        }
      }
    };

    fetchProgress();
  }, [moduleId]);
  if (!overview) return <p>Loading...</p>;

  // Always check if exists first else empty/temp value
  const moduleTitle =
    overview?.module
      ? overview.module.moduleCode + " " + overview.module.moduleName
      : "COMXXXX UNKNOWN";

  const assessments = overview?.assessments ?? [];

  const stages = overview?.stages ?? [];

  const CARDS = assessments.map(a => ({
    key: a.id,
    // capitalized assessment type
    assessmentType: a.type[0] + a.type.substring(1).toLowerCase(),
    title: a.name,
    description: a.description,
    // Show stage/total stages for progress status
    status: "Stage: " + (stages[a.assessmentStageId - 1]?.step ?? "0")
      + "/" + (stages?.filter(s => s.assessmentType === a.type)).length, //getStage(a.assessmentStageId),
    type: a.isComplete ? "ok" : "warn"
  }
  ))

  return (
    <>
      <div className="ao-wrap">
        <h2 className="ao-subtitle">Assessment Overview</h2>

        <div className="ao-grid">
          {CARDS.map((c) => (
            <div key={c.key} className="ao-card">
              <h4 className="ao-card-title">{c.title}</h4>

              <div className="ao-card-details">{c.assessmentType}</div>
              <div className="ao-card-description">{c.description}</div>
              <div className={`ao-pill ${c.type}`}>{c.status}</div>

              <button
                className="ao-arrow"
                onClick={() => navigate(`/modules/${moduleId}/assessments/${c.key}/progress`)}
                aria-label={`Open ${c.title}`}
              >
                »
              </button>
            </div>
          ))}
        </div>

        <button
          className="ao-primary"
          onClick={() => navigate(`/modules/${moduleId}/assessments/new`)}
        >
          Create New Assessment
        </button>
      </div>
    </>
  );
}
