import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Axios from "axios";
import Navbar from "../components/Navbar.jsx";
import "./AssessmentOverview.css";
import ModuleInfo from "../components/ModuleInfo.jsx";
import StatusLegend from "../components/StatusLegend.jsx";

export default function AssessmentOverview() {
  const navigate = useNavigate();
  if (!localStorage.getItem("token")) { navigate("/login") }
  // example url: /modules/assessments/1
  const moduleId = useParams().moduleId;
  const role = localStorage.getItem("role");

  const [showAll, setShowAll] = useState(false);

  // fetch data from api
  const [overview, setOverview] = useState(null);
  useEffect(() => {
    const fetchOverview = async () => {
      try {
        const response = await Axios.get(
          ((role === "ADMIN" || role === "EXAMS_OFFICER") && (!moduleId)
            ? `http://localhost:8080/api/assessments`
            : `http://localhost:8080/api/modules/${moduleId}/assessments`),
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

    fetchOverview();
  }, [moduleId]);
  if (!overview) return <p>Loading...</p>;

  // Always check if exists first else empty/temp value
  const assessments = overview?.assessments ?? [];
  const stages = overview?.stages ?? [];

  const CARDS = assessments?.map(a => ({
    key: a.id,
    // capitalized assessment type
    assessmentType: a.type[0] + a.type.substring(1).toLowerCase(),
    title: a.name,
    description: a.description,
    // Show stage/total stages for progress status
    status: "Stage: " + (stages[a.assessmentStageId - 1]?.step ?? "0")
      + "/" + (stages?.filter(s => s.assessmentType === a.type)).length, //getStage(a.assessmentStageId),
    type: (a.isActive ? (a.isComplete ? "ok" : "warn") : "danger"),
    show: a.isActive || (role === "ADMIN" || role === "EXAMS_OFFICER"),
    moduleId: a.moduleId
  }
  ))

  const toggleAssessmentActivity = async (assessmentId) => {
    try {
      const url = `http://localhost:8080/api/assessments/${assessmentId}/activity`;

      const response = await Axios.post(
        url,
        {}, // empty body
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      console.log("Success:", response.data);

      // Refresh the view
      window.location.reload();
    } catch (error) {
      console.error("Failed to toggle assessment activity:", error);

      if (error.response) {
        console.error("Status:", error.response.status);
        console.error("Data:", error.response.data);
      }
    }
  };

  return (
    <>
      <div className="ao-wrap">
        {moduleId && (
          <>
            <ModuleInfo module={overview?.modules[0]} />
            <h2 className="ao-subtitle">Assessment Overview</h2>
            <hr />
          </>
        )}
        <StatusLegend />

        <div className="ao-grid">
          {CARDS.map((c) => (c.show &&
            <div key={c.key} className="ao-card">
              <h4 className="ao-card-title">{c.title}</h4>

              <div className="ao-card-details">{c.assessmentType}</div>
              <div className="ao-card-description">{c.description}</div>
              <div className={`ao-pill ${c.type}`}>{c.status}</div>

              {(c.type != "danger") && ["ADMIN", "EXAMS_OFFICER"].includes(role) && (
                <button
                  className="ao-delete"
                  onClick={() => toggleAssessmentActivity(c.key)}
                  aria-label={`Delete ${c.title}`}
                >
                  Delete 🗑
                </button>
              )}

              {(c.type === "danger") && ["ADMIN", "EXAMS_OFFICER"].includes(role) && (
                <button
                  className="ao-delete"
                  onClick={() => toggleAssessmentActivity(c.key)}
                  aria-label={`Restore ${c.title}`}
                >
                  Restore ♲
                </button>
              )}

              <button
                className="ao-details"
                onClick={() =>
                  navigate(`/modules/${c.moduleId}/assessments/${c.key}/progress`)
                }
                aria-label={`Open ${c.title}`}
              >
                Details »
              </button>

              <button
                className="ao-logs-button"
                onClick={() =>
                  navigate(`/modules/${moduleId}/assessments/${c.key}/logs`)
                }
                aria-label={`View logs for ${c.title}`}
              >
                View logs
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
