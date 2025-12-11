import React from "react";
import "./ModuleInfo.css";

export default function ModuleInfo({ module }) {
  if (!module) return null;

  const { moduleCode, moduleName, moduleStaff } = module;

  return (
    <div className="module-info">
      <h2 className="module-title">
        {moduleCode} — {moduleName}
      </h2>

      <div className="module-staff-grid">
        {moduleStaff.map((staff) => (
          <div key={staff.staffId} className="module-staff-card">
            <div className="module-staff-name">
              {staff.forename} {staff.surname}
            </div>
            <div className="module-staff-email">{staff.email}</div>
            <div className="module-staff-role">
              {staff.moduleRole.replace(/_/g, " ")}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

