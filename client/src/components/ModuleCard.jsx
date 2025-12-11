import React from "react";
import { Link } from "react-router-dom";
import "../index.css"
import "./ModuleCard.css"
import dropdownIcon from "../assets/moduleCardDropdown.png"

// Dropdown arrow source: "https://www.flaticon.com/free-icons/down-arrow"

export default function ModuleCard({ moduleId, moduleCode, moduleTitle, isOpen, onToggle }) {
  return (
    <div className="module-box">
      <h2 className="module-code">{moduleCode}</h2>
      <div className="module-info">
        <div className="module-title-dropdown-row">
          <p className="module-title">{moduleTitle}</p>
          <button className="dropdown-btn" onClick={onToggle}>
            <img className="dropdown-icon" src={dropdownIcon}></img>
          </button>
          {isOpen && (
            <div className="dropdown-content">
              <Link to={`/modules/${moduleId}/assessments`} className="dropdown-link">See assignments</Link>
              <Link to={`/modules/edit/${moduleCode}`} className="dropdown-link">Edit module details</Link>
              <Link to={`/modules/delete/${moduleCode}`} className="dropdown-link">Delete module</Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
