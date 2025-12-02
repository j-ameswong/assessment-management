import React from "react";
import { Link } from "react-router-dom";
import "../componentStyles/ModuleCard.css"
import dropdownIcon from "../assets/moduleCardDropdown.png"

// Dropdown arrow source: "https://www.flaticon.com/free-icons/down-arrow"

export default function ModuleCard({ moduleCode, moduleTitle, isOpen, onToggle }) {
    return (
        <div className="module-box">
            <h2 className="module-code">{moduleCode}</h2>
            <div className="module-info">
                <div className="module-title-dropdown-row">
                    <h2 className="module-title">{moduleTitle}</h2>
                    <button className="dropdown-btn" onClick={onToggle}>
                        <img className="dropdown-icon" src={dropdownIcon}></img>
                    </button>
                    { isOpen && (
                        <div className="dropdown-content">
                            <Link to="/modules/assessments" className="dropdown-link">See assignments</Link>
                            <Link to="/modules/assessments" className="dropdown-link">Edit module details</Link>
                            <Link to="/modules/delete" className="dropdown-link">Delete module</Link>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}