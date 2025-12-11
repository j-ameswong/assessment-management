import React from "react";
import { Link } from "react-router-dom";
import "../index.css"
import "./ModuleCard.css"
import dropdownIcon from "../assets/moduleCardDropdown.png"

// Dropdown arrow source: "https://www.flaticon.com/free-icons/down-arrow"

export default function ModuleCard({ module, userId, role, isOpen, onToggle }) {
  const contextRole = (role == "EXAMS_OFFICER" || role == "ADMIN")
    ? role
    : module?.moduleStaff.find(s => s.staffId == userId)?.moduleRole ?? "PLACEHOLDER"


  const toCapitalize = (str) => {
    let newStr = str.replaceAll("_", " ");
    newStr = newStr[0] + newStr.slice(1).toLowerCase();

    return newStr;
  }

  return (
    <div className="module-box">
      <Link to={`/modules/${module.id}/assessments`} className="module-code">{module.moduleCode}</Link>
      <div className="module-info">
        <div className="module-title-dropdown-row">
          <div className="module-title">
            <p>{module.moduleName}</p>
            {`(${toCapitalize(contextRole)})`}
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
