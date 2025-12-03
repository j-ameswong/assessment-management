import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Navbar from "../components/Navbar.jsx";
import "./AssessmentOverview.css";

export default function AssessmentOverview() {
    const navigate = useNavigate();
    // example url: /modules/assessments/1
    const moduleId = useParams().moduleId;

    // fetch assessments in module from api
    const [overview, setOverview] = useState(null);
    useEffect(() => {
        if (!moduleId) { return }
        fetch("http://localhost:8080/api/modules/" + moduleId + "/overview")
            .then(res => res.json())
            .then(data => setOverview(data))
            .catch(err => console.error(err));
    }, [moduleId]);

    const moduleTitle =
        overview?.module
            ? overview.module.moduleCode + " " + overview.module.moduleName
            : "Unknown Module";

    const assessments = overview?.assessments ?? [];

    const stages = overview?.stages;

    const CARDS = assessments.map(a => ({
        key: a.id,
        title: a.name,
        status: "Stage: " + (stages[a.assessmentStageId - 1]?.step ?? "0")
            + "/" + (stages?.filter(s => s.assessmentType === a.type)).length, //getStage(a.assessmentStageId),
        type: "ok"
    }
    ))

    return (
        <>
            <Navbar left={moduleTitle} right="Exam officer" />

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
