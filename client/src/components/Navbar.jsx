import React from "react";

export default function Navbar({
  left = "COM2008 Systems Design and Security",
  right = "Exam officer",
}) {
  return (
    <header
      style={{
        position: "sticky",
        top: 0,
        left: 0,
        width: "100%",
        zIndex: 1000,
        background: "#2d0c73",
        color: "#fff",
      }}
    >
      <div
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          width: "100%",
          boxSizing: "border-box",
          padding: "12px 24px",
          fontWeight: 700,
        }}
      >
        <span style={{ fontSize: 20 }}>{left}</span>
        <span>{right} ▾</span>
      </div>
    </header>
  );
}
