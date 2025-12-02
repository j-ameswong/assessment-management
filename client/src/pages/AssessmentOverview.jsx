import React, { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import Navbar from "../components/Navbar.jsx";
import "./AssessmentOverview.css";

// const CARDS = [
//     { key: "coursework", title: "Coursework", status: "No action required", type: "ok" },
//     { key: "in-semester-quiz", title: "In-semester quiz", status: "Action required", type: "danger" },
//     { key: "exams", title: "Exams", status: "In progress", type: "warn" },
// ];
export default function AssessmentOverview() {
    const navigate = useNavigate();
    // example url: /modules/assessments?moduleId=1
    const [params, setParams] = useSearchParams();
    const [assessments, setAssessments] = useState([]);
    const moduleId = params.get("moduleId");
    useEffect(() => {
        fetch("http://localhost:8080/api/modules/" + moduleId + "/assessments")
            .then(res => res.json())
            .then(data => setAssessments(data))
            .catch(err => console.error(err));
    }, []);

    const CARDS = assessments.map(a => ({
        key: a.id,
        title: a.name,
        status: "In progress",
        type: "ok"
    }
    ))

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
