import React from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar.jsx";
import "./AssessmentOverview.css";

const CARDS = [
  { key: "coursework",        title: "Coursework",        status: "No action required", type: "ok" },
  { key: "in-semester-quiz",  title: "In-semester quiz",  status: "Action required",    type: "danger" },
  { key: "exams",             title: "Exams",             status: "In progress",        type: "warn" },
];

export default function AssessmentOverview() {
  const navigate = useNavigate();

  return (
    <>
      <Navbar left="COM2008 Systems Design and Security" right="Exam officer" />

      <div className="ao-wrap">
        <h2 className="ao-subtitle">Assessment Overview</h2>

        <div className="ao-grid">
          {CARDS.map((c) => (
            <div key={c.key} className="ao-card">
              <h3 className="ao-card-title">{c.title}</h3>

              <div className={`ao-pill ${c.type}`}>{c.status}</div>

              <button
                className="ao-arrow"
                onClick={() => navigate(`/modules/assessments/${c.key}`)}
                aria-label={`Open ${c.title}`}
              >
                »
              </button>
            </div>
          ))}
        </div>

        <button
          className="ao-primary"
          onClick={() => navigate("/modules/assessments/new")}
        >
          Create New Assessment
        </button>
      </div>
    </>
  );
}
