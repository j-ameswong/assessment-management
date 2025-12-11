import React from "react";
import { Link } from "react-router-dom";
import "../index.css"
import "./ModuleCard.css"
import dropdownIcon from "../assets/moduleCardDropdown.png"

// Dropdown arrow source: "https://www.flaticon.com/free-icons/down-arrow"

export default function ModuleCard({ module, userId, isOpen, onToggle }) {
  const contextRole = module?.moduleStaff.find(s => s.staffId === userId)?.moduleRole ?? "Staff";
  return (
    <div className="module-box">
      <Link to={`/modules/${module.id}/assessments`} className="module-code">{module.moduleCode}</Link>
      <div className="module-info">
        <div className="module-title-dropdown-row">
          <div className="module-title">
            <p>{module.moduleName}</p>
            {`(${contextRole.split(1)})`}
          </div>
          <button className="dropdown-btn" onClick={onToggle}>
            <img className="dropdown-icon" src={dropdownIcon}></img>
          </button>
          {isOpen && (
            <div className="dropdown-content">
              <Link to={`/modules/${module.id}/assessments`} className="dropdown-link">See assignments</Link>
              <Link to={`/modules/edit/${module.moduleCode}`} className="dropdown-link">Edit module details</Link>
              <Link to={`/modules/delete/${module.moduleCode}`} className="dropdown-link">Delete module</Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
