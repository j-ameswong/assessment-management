import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axiosConfig";

export default function MyTasks() {
  const [loading, setLoading] = useState(true);
  const [modules, setModules] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [roleFilter, setRoleFilter] = useState("ALL");

  const userId = Number(localStorage.getItem("userId"));
  const primaryRole = localStorage.getItem("role");

  useEffect(() => {
    async function fetchModules() {
      try {
        const url =
          primaryRole === "ADMIN" || primaryRole === "EXAMS_OFFICER"
            ? "/modules"
            : `/modules/user/${userId}`;
        const res = await api.get(url);
        setModules(res.data || []);
      } catch (e) {
        console.error("Failed to fetch modules", e);
      }
    }
    fetchModules();
  }, [primaryRole, userId]);

  useEffect(() => {
    if (!modules.length) {
      setLoading(false);
      setTasks([]);
      return;
    }

    async function buildTasks() {
      try {
        const allTasks = [];

        for (const mod of modules) {
          const overviewRes = await api.get(`/modules/${mod.id || mod.moduleId || mod.moduleCode}/assessments`);
          const overview = overviewRes.data;

          const assessments = overview?.assessments ?? [];
          const stages = overview?.stages ?? [];

          const activeAssessments = assessments.filter(a => a.isActive);

          for (const a of activeAssessments) {
            try {
              const progRes = await api.get(`/assessments/${a.id}/progress`);
              const progress = progRes.data;

              const moduleDto = progress.module;
              const assessment = progress.assessment;
              const assessmentStages = progress.assessmentStages || []; 
              const currentStage = assessmentStages.find(s => s.id === assessment.assessmentStageId);

              const roles = [primaryRole, "ANY"];
              const myModuleStaff = moduleDto?.moduleStaff?.find(s => s.staffId === userId);
              if (myModuleStaff?.moduleRole) roles.push(myModuleStaff.moduleRole);
              if (assessment?.setterId === userId) roles.push("SETTER");
              if (assessment?.checkerId === userId) roles.push("CHECKER");
              if (assessment?.externalExaminer?.id === userId) roles.push("EXTERNAL_EXAMINER");

              const requiredRole = currentStage?.actor;
              const isMine = requiredRole && roles.includes(requiredRole);

              if (isMine) {
                const moduleCode = moduleDto?.moduleCode || mod.moduleCode || "";
                const moduleName = moduleDto?.moduleName || mod.moduleName || "";

                allTasks.push({
                  assessmentId: assessment.id,
                  assessmentTitle: assessment.name || assessment.title || "(untitled)",
                  assessmentType: assessment.type, // cw/test/exam
                  moduleId: moduleDto?.id || mod.id,
                  moduleCode,
                  moduleName,
                  currentStageName: currentStage?.description || currentStage?.name || "",
                  requiredRole,
                });
              }
            } catch (inner) {
              console.error(`progress failed for assessment ${a.id}`, inner);
            }
          }
        }

        setTasks(allTasks);
      } catch (e) {
        console.error("Failed to build tasks", e);
      } finally {
        setLoading(false);
      }
    }

    buildTasks();
  }, [modules, primaryRole, userId]);

  const filtered = useMemo(() => {
    if (roleFilter === "ALL") return tasks;
    return tasks.filter(t => t.requiredRole === roleFilter);
  }, [tasks, roleFilter]);

  // UI
  if (loading) return <div style={{ padding: 16 }}>Loading my tasks…</div>;

  return (
    <div style={{ padding: 16 }}>
      <h1>My tasks</h1>

      <div style={{ margin: "12px 0" }}>
        <label style={{ marginRight: 8 }}>Filter by required role</label>
        <select value={roleFilter} onChange={e => setRoleFilter(e.target.value)}>
          <option value="ALL">All</option>
          <option value="CHECKER">Checker</option>
          <option value="SETTER">Setter</option>
          <option value="MODERATOR">Moderator</option>
          <option value="EXAMS_OFFICER">Exams officer</option>
          <option value="EXTERNAL_EXAMINER">External examiner</option>
        </select>
        <span style={{ marginLeft: 12, color: "#666" }}>
          {filtered.length} task(s)
        </span>
      </div>

      {filtered.length === 0 && (
        <div style={{ padding: 12, background: "#f6f6f6", border: "1px solid #eee" }}>
          No pending tasks. 🎉
        </div>
      )}

      <ul style={{ listStyle: "none", paddingLeft: 0 }}>
        {filtered.map(item => (
          <li key={item.assessmentId}
              style={{ border: "1px solid #eee", borderRadius: 8, padding: 12, marginTop: 8 }}>
            <div style={{ fontWeight: 600 }}>
              {item.assessmentTitle} <span style={{ fontWeight: 400, color: "#666" }}>({item.assessmentType})</span>
            </div>
            <div style={{ fontSize: 14, color: "#555", marginTop: 4 }}>
              {item.moduleCode} {item.moduleName} · Stage: {item.currentStageName} · Required: {item.requiredRole}
            </div>
            <div style={{ marginTop: 8 }}>
              <Link to={`/modules/${item.moduleId}/assessments/${item.assessmentId}/progress`}>Go to progression</Link>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
