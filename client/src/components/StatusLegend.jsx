import React from "react";
import "./StatusLegend.css";

export default function StatusLegend() {
  return (
    <div className="legend">
      <LegendItem color="deleted" label="Deleted" />
      <LegendItem color="in-progress" label="In Progress" />
      <LegendItem color="completed" label="Completed" />
    </div>
  );
}

function LegendItem({ color, label }) {
  return (
    <div className="legend-item">
      <div className={`dot ${color}`}></div>
      <span>{label}</span>
    </div>
  );
}
