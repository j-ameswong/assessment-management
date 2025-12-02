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
    const moduleId = params.get("moduleId");

    // fetch assessments in module from api
    const [assessments, setAssessments] = useState([]);
    useEffect(() => {
        fetch("http://localhost:8080/api/modules/" + moduleId + "/assessments")
            .then(res => res.json())
            .then(data => setAssessments(data))
            .catch(err => console.error(err));
    }, []);

    // get module by moduleId
    const [module, setModule] = useState({});
    useEffect(() => {
        fetch("http://localhost:8080/api/modules/" + moduleId)
            .then(res => res.json())
            .then(data => setModule(data))
            .catch(err => console.error(err));
    }, []);
    const moduleTitle = module.moduleCode + " " + module.moduleName;

    const CARDS = assessments.map(a => ({
        key: a.id,
        title: a.name,
        status: "In progress",
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
