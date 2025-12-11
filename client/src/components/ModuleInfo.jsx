import React from "react";
import "./ModuleInfo.css";

export default function ModuleInfo({ module }) {
  if (!module) return null;

  const { moduleCode, moduleName, moduleStaff } = module;

  return (
    <div className="module-info-card">
      <h3 className="module-info-title">{moduleCode} — {moduleName}</h3>

      <div className="module-info-grid">
        {moduleStaff.map(staff => (
          <div key={staff.staffId} className="module-staff-card">
            <span className="module-staff-name">
              {staff.forename} {staff.surname}
            </span>

            <span className="module-staff-email">{staff.email}</span>

            <span className="module-staff-role">
              {staff.moduleRole.replace(/_/g, " ")}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}
