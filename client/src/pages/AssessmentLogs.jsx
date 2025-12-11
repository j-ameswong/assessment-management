import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Axios from "axios";
import "./AssessmentLogs.css";

export default function AssessmentLogs() {
  const navigate = useNavigate();
  const { moduleId, assessmentId } = useParams();

  useEffect(() => {
    if (!localStorage.getItem("token")) {
      navigate("/");
    }
  }, [navigate]);

  const [progress, setProgress] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [sortBy, setSortBy] = useState("time-desc");
  const [roleFilter, setRoleFilter] = useState("ALL"); 

  useEffect(() => {
    if (!assessmentId) return;

    const fetchProgress = async () => {
      try {
        const response = await Axios.get(
          `http://localhost:8080/api/assessments/${assessmentId}/progress`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        setProgress(response.data);
      } catch (err) {
        console.error("Failed to fetch assessment logs:", err);
        setError("Failed to load logs.");
      } finally {
        setLoading(false);
      }
    };

    fetchProgress();
  }, [assessmentId]);

  if (loading) {
    return <p>Loading logs...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  if (!progress) {
    return <p>No data found for this assessment.</p>;
  }

  const { module, assessment, assessmentStages = [], assessmentStageLogs = [] } = progress;

  const flatLogs = assessmentStageLogs.map((log) => {
    const stage = assessmentStages.find((s) => s.id === log.assessmentStageId);
    const actorName = log.actor
      ? `${log.actor.forename} ${log.actor.surname}`
      : "Unknown actor";

    return {
      ...log,
      actorName,
      role: stage?.actor || "UNKNOWN", 
      stageDescription: stage?.description || "",
      step: stage?.step ?? null,
    };
  });

  const uniqueRoles = Array.from(new Set(flatLogs.map((l) => l.role)))
    .filter(Boolean)
    .sort();

  const filteredLogs = flatLogs.filter((log) =>
    roleFilter === "ALL" ? true : log.role === roleFilter
  );

  const sortedLogs = [...filteredLogs].sort((a, b) => {
    if (sortBy === "time-asc") {
      return new Date(a.changedAt) - new Date(b.changedAt);
    }
    if (sortBy === "actor") {
      return a.actorName.localeCompare(b.actorName);
    }
            return new Date(b.changedAt) - new Date(a.changedAt);
  });

    const handleBack = () => {
        navigate(-1);
    };


  return (
    <div className="logs-page">
      <div className="logs-header">
        <h2>Assessment Logs</h2>
        <p>
          <strong>Module:</strong>{" "}
          {module ? `${module.moduleCode} ${module.moduleName}` : "Unknown module"}
        </p>
        <p>
          <strong>Assessment:</strong>{" "}
          {assessment ? assessment.name : "Unknown assessment"}
        </p>
        <button className="logs-back-btn" onClick={handleBack}>
          ← Back
        </button>
      </div>

      <div className="logs-controls">
        <label>
          Sort by:&nbsp;
          <select value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
            <option value="time-desc">Time: newest first</option>
            <option value="time-asc">Time: oldest first</option>
            <option value="actor">Actor name A–Z</option>
          </select>
        </label>

        <label>
          &nbsp;Filter by role:&nbsp;
          <select value={roleFilter} onChange={(e) => setRoleFilter(e.target.value)}>
            <option value="ALL">All roles</option>
            {uniqueRoles.map((role) => (
              <option key={role} value={role}>
                {role.replaceAll("_", " ")}
              </option>
            ))}
          </select>
        </label>
      </div>

      {sortedLogs.length === 0 ? (
        <p>No logs recorded for this assessment.</p>
      ) : (
        <table className="logs-table">
          <thead>
            <tr>
              <th>Time</th>
              <th>Stage</th>
              <th>Role</th>
              <th>Actor</th>
              <th>Status</th>
              <th>Note</th>
            </tr>
          </thead>
          <tbody>
            {sortedLogs.map((log) => (
              <tr key={log.id}>
                <td>{new Date(log.changedAt).toLocaleString()}</td>
                <td>
                  {log.step != null ? `${log.step}. ` : ""}
                  {log.stageDescription}
                </td>
                <td>{log.role.replaceAll("_", " ")}</td>
                <td>{log.actorName}</td>
                <td>{log.isComplete ? "Complete" : "Pending"}</td>
                <td>{log.note || ""}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
