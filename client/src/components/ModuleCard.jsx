import React from "react";
import { useState } from 'react';
import { Link } from "react-router-dom";
import "../componentStyles/ModuleCard.css"

export default function ModuleCard({ moduleCode, moduleTitle }) {
    const [ddOpen, setDdOpen] = useState(false);

    const toggleDropdown = () => {
        setDdOpen(prev => !prev)
    };

    return (
        <div className="module-box">
            <h2 className="module-code">{moduleCode}</h2>
            <div className="module-info">
                <div className="module-title-dropdown-row">
                    <h2 className="module-title">{moduleTitle}</h2>
                    <div className="dropdown">
                        <button className="dropdown-btn" onClick={toggleDropdown}>
                            Dropdown
                        </button>
                        { ddOpen && (
                            <div className="dropdown-content">
                                <Link to="/modules/assessments" className="dropdown-link">See assignments</Link>
                                <Link to="/modules/assessments" className="dropdown-link">Edit module details</Link>
                                <Link to="/modules/assessments" className="dropdown-link">Delete modules</Link>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}