import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

import axios from "axios";
import "./CreateAssessment.css"

export default function CreateAssessment() {
  const navigate = useNavigate();
  if (!localStorage.getItem("token")) { navigate("/login") }

  const [name, setName] = useState("");
  const [type, setType] = useState(null);
  const [description, setDescription] = useState("");

  const [csvFile, setCsvFile] = useState(null);
  const [attachment, setAttachment] = useState(null);

  // Module list
  const [modules, setModules] = useState([]);
  const { moduleId: routeModuleId } = useParams();
  const [moduleId, setModuleId] = useState(routeModuleId ? Number(routeModuleId) : 0);


  // Staff lists
  const [moduleStaff, setModuleStaff] = useState([]);
  const [checkerCandidates, setCheckerCandidates] = useState([]);
  const [externalExaminers, setExternalExaminers] = useState([]);

  // Setter/Checker/External Examiner
  const [setterId, setSetterId] = useState('');
  const [checkerId, setCheckerId] = useState('');
  const [externalExaminerId, setExternalExaminerId] = useState('');


  // deadline
  const [deadline, setDeadline] = useState("");


  useEffect(() => {
    const role = localStorage.getItem("role");
    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");


    const url =
      role === "ADMIN" || role === "EXAMS_OFFICER"
        ? "http://localhost:8080/api/modules"
        : `http://localhost:8080/api/modules/user/${userId}`;
    fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    })
      .then(res => {
        if (!res.ok) {
          throw new Error("Failed to fetch modules");
        }
        return res.json();
      })
      .then(data => setModules(data))
      .catch(err => console.error(err));

  }, []);


  // When modules load or route moduleId changes, auto select module
  useEffect(() => {
    if (!modules || modules.length === 0) return;

    const autoSelect = () => {
      if (routeModuleId) {
        setModuleId(Number(routeModuleId));
        onModuleSelect({ target: { value: Number(routeModuleId) } });
      }
    };

    autoSelect();
  }, [modules, routeModuleId]);

  useEffect(() => {
    if (routeModuleId && !isNaN(routeModuleId)) {
      setModuleId(Number(routeModuleId));
    }
  }, [routeModuleId]);

  const onModuleSelect = async (event) => {

    const id = Number(event.target.value);
    setModuleId(id);
    if (!id) return;

    const token = localStorage.getItem("token");

    try {
      // fetch the staff of this module
      const resModuleStaff = await axios.get(
        `http://localhost:8080/api/modules/${id}/staff`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      const staff = resModuleStaff.data;
      setModuleStaff(staff);

      // default setter = module lead
      const lead = staff.find(s => s.moduleRole === "MODULE_LEAD");
      if (lead) {
        setSetterId(String(lead.staffId));
      } else {
        setSetterId('');
      }

      const moderator = staff.find(s => s.moduleRole === "MODERATOR");

      const resAll = await axios.get(
        "http://localhost:8080/api/users",
        { headers: { Authorization: `Bearer ${token}` } }
      );

      const allUsers = resAll.data;

      const externalList = allUsers.filter(u => u.role === "EXTERNAL_EXAMINER");
      setExternalExaminers(externalList);

      let checkerList = allUsers.filter(u =>
        ["ACADEMIC_STAFF", "EXAMS_OFFICER"].includes(u.role) &&
        !staff.some(ms => ms.staffId === u.id)
      );

      if (moderator) {
        const moderatorCandidate = {
          id: moderator.staffId,
          forename: moderator.forename,
          surname: moderator.surname,
          role: "MODERATOR"
        };

        checkerList = [moderatorCandidate, ...checkerList];

        setCheckerId(String(moderator.staffId));
      } else {
        setCheckerId('');
      }

      setCheckerCandidates(checkerList);

    } catch (err) {
      console.error("Failed to load staff", err);
    }

  };




  const Create = () => {
    if (!name || !type || !moduleId || !setterId || !checkerId) {
      alert("Please enter name and select a type.");
      return;
    }

    const payload = {
      name: name,
      type: type.toUpperCase(),
      moduleId: moduleId,
      setterId: setterId,
      checkerId: checkerId,
      externalExaminer: externalExaminerId,
      description: description,
      deadline: deadline ? new Date(deadline).toISOString() : null

    };

    console.log("payload send:", payload);

    axios.post("http://localhost:8080/api/assessments", payload, {
      headers: { Authorization: `Bearer ${localStorage.getItem("token")}` }
    })
      .then(response => {
        console.log("Created:", response.data);
        alert("Assessment created!");
      })
      .catch(error => {
        console.error(error);
        alert("Failed to create assessment.");
      });
  };

  // Upload CSV
  const CsvUpload = () => {
    const formData = new FormData();
    formData.append("file", csvFile);

    axios.post("http://localhost:8080/api/assessments/uploadCsv", formData, {
      headers: { "Content-Type": "multipart/form-data" }
    })
      .then(() => alert("CSV uploaded!"))
      .catch(() => alert("Failed to upload CSV."));
  };

  // Upload Attachment
  const AttachmentUpload = () => {
    const formData = new FormData();
    formData.append("file", attachment);
    axios.post("http://localhost:8080/api/assessments/uploadAttachment", formData, {
      headers: { "Content-Type": "multipart/form-data" }
    })
    alert("Attachment uploaded!");
  }

  return (
    <>
      <div className="assessment-page">
        <div className="assessment-container">

          <h1 className="title">Create New Assessment</h1>

          <div className="grid-container">

            {/* left area */}
            <div className="left-column">

              {/* Name */}
              <label className="label">Assessment Name</label>
              <input
                className="input"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />


              {/*Assessment Type*/}
              <label className="label">Assessment Type</label>
              <div className="type-buttons">
                <button
                  className={`type-btn ${type === "COURSEWORK" ? "selected" : ""}`}
                  onClick={() => setType("COURSEWORK")}
                  onDoubleClick={() => setType(null)}
                >
                  <span>Coursework</span>
                  <span className={`circle ${type === "COURSEWORK" ? "checked" : ""}`}>
                    {type === "COURSEWORK" && <span className="tick">✔</span>}
                  </span>
                </button>
                <button
                  className={`type-btn ${type === "test" ? "selected" : ""}`}
                  onClick={() => setType("test")}
                  onDoubleClick={() => setType(null)}
                >
                  <span>In-semester quiz</span>
                  <span className={`circle ${type === "test" ? "checked" : ""}`}>
                    {type === "test" && <span className="tick">✔</span>}
                  </span>
                </button>
                <button
                  className={`type-btn ${type === "exam" ? "selected" : ""}`}
                  onClick={() => setType("exam")}
                  onDoubleClick={() => setType(null)}
                >
                  <span>Exam</span>
                  <span className={`circle ${type === "exam" ? "checked" : ""}`}>
                    {type === "exam" && <span className="tick">✔</span>}
                  </span>
                </button>
              </div>

              <div className="or">
                <p>OR</p>
              </div>

              <label className="label">Upload .csv file</label>
              <div className="csv-wrapper">
                <input
                  className="csv-input"
                  type="file"
                  accept=".csv"
                  onChange={(e) => setCsvFile(e.target.files[0])}

                />

                <div className="file-display">
                  <img src="/icons/file.svg" alt="file upload icon" className="file-icon" />
                </div>

                <button className="attach-btn" onClick={CsvUpload}>
                  Attach
                </button>
              </div>

              <label className="label">Attachments</label>
              <div className="attach-wrapper">
                <input
                  className="attach-input"
                  type="file"
                  onChange={(e) => setAttachment(e.target.files[0])}
                />

                <div className="file-display">
                  <img src="/icons/file.svg" alt="file upload icon" className="file-icon" />
                </div>

                <button className="attach-btn" onClick={AttachmentUpload}>Attach</button>
              </div>

              <label className="label">Deadline</label>
              <input
                type="datetime-local"
                className="input"
                value={deadline}
                onChange={e => setDeadline(e.target.value)}
              />

            </div>

            {/* right area */}
            <div className="right-column">

              {/* Module */}
              <label className="label">For Module</label>
              <select
                value={Number.isNaN(moduleId) ? "" : moduleId}
                onChange={onModuleSelect}
              >
                <option value="">-- Select Module --</option>
                {modules.map((m) => (
                  <option key={m.id} value={m.id}>
                    {m.moduleName}
                  </option>
                ))}
              </select>

              {/*Setter / Checker / External Examiner dropdowns (only show if module selected)*/}
              {moduleId !== 0 && (
                <>
                  <label className="label">Setter</label>
                  <select value={setterId} onChange={e => setSetterId(e.target.value)}>
                    <option value="">-- Select Setter --</option>
                    {moduleStaff
                      .filter(s => s.moduleRole !== "MODERATOR")
                      .map(s => (
                        <option
                          key={s.staffId}
                          value={String(s.staffId)}
                        >
                          {s.forename} {s.surname} — {s.moduleRole}
                        </option>
                      ))}
                  </select>


                  <label className="label">Checker</label>
                  <select value={checkerId} onChange={e => setCheckerId(e.target.value)}>
                    <option value="">-- Select Checker --</option>
                    {checkerCandidates.map(s => (
                      <option key={s.id} value={s.id}>
                        {s.forename} {s.surname} — {s.role}
                      </option>
                    ))}
                  </select>

                  <label className="label">External Examiner</label>
                  <select value={externalExaminerId} onChange={e => setExternalExaminerId(e.target.value)}>
                    <option value="">-- Select External Examiner --</option>
                    {externalExaminers.map(e => (
                      <option key={e.id} value={e.id}>
                        {e.forename} {e.surname}
                      </option>
                    ))}
                  </select>

                </>
              )}

              <label className="label">Description</label>
              <textarea
                className="textarea"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
              ></textarea>

              <button className="create-btn" onClick={Create}>
                Create
              </button>
            </div>

          </div>

        </div>

      </div>
    </>
  );
}
