import React from "react";
// Dropdown state is now managed by parent
import { Link } from "react-router-dom";
import "../componentStyles/ModuleCard.css"

export default function ModuleCard({ moduleCode, moduleTitle, isOpen, onToggle }) {
    return (
        <div className="module-box">
            <h2 className="module-code">{moduleCode}</h2>
            <div className="module-info">
                <div className="module-title-dropdown-row">
                    <h2 className="module-title">{moduleTitle}</h2>
                    <button className="dropdown-btn" onClick={onToggle}>
                        Dropdown
                    </button>
                    { isOpen && (
                        <div className="dropdown-content">
                            <Link to="/modules/assessments" className="dropdown-link">See assignments</Link>
                            <Link to="/modules/assessments" className="dropdown-link">Edit module details</Link>
                            <Link to="/modules/assessments" className="dropdown-link">Delete module</Link>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}