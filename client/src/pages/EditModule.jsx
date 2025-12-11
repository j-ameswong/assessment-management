
import { useEffect, useState } from "react";
import axios from "axios";
import Select from "react-select";
import { useParams, useNavigate } from "react-router-dom";
import "./EditModule.css";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";


export default function EditModule() {
  // Module Fields
  const { moduleCode } = useParams();
  const [currentModule, setCurrentModule] = useState(null);

  const [name, setName] = useState("");
  const [newModCode, setnewModCode] = useState("");
  const [modLead, setModLead] = useState("");
  const [modModerator, setModModerator] = useState("");
  const [modStaff, setModStaff] = useState([]);
  const navigate = useNavigate();

  if (!localStorage.getItem("token")) { navigate("/login") }
  // Fetch Staff
  const [staffList, setStaffList] = useState([]);
  useEffect(() => {
    fetch("http://localhost:8080/api/users", {
      headers: {
        "Authorization": `Bearer ${localStorage.getItem("token")}`,
        "Content-Type": "application/json"
      }
    })
      .then(res => {
        console.log("Staff Status code: ", res.status); // for debugging
        return res.json();
      })
      .then(data => {
        console.log("Fetched users: ", data);
        setStaffList(data);
      })
      .catch(err => console.error("Error loading staff members: ", err));
  }, []);

  // Fetch current module and staff
  useEffect(() => {
    fetch(`http://localhost:8080/api/modules/edit/${moduleCode}`, {
      headers: {
        "Authorization": `Bearer ${localStorage.getItem("token")}`,
        "Content-Type": "application/json"
      }
    })
      .then(res => {
        console.log("Module Status code: ", res.status);
        return res.json();
      })
      .then(data => {
        console.log("Fetched module: ", data);
        setCurrentModule(data);

      })
      .catch(err => console.error("Error loading staff members: ", err));
  }, []);

  const modStaffOptions = staffList.map(user => ({
    value: user.id,
    label: `${user.forename} ${user.surname}`
  }));

  const filteredLeadOptions = modStaffOptions.filter(option =>
    option.value !== modModerator && !modStaff.includes(option.value)
  )
  const filteredModeratorOptions = modStaffOptions.filter(option =>
    option.value !== modLead && !modStaff.includes(option.value)
  );
  const filteredStaffOptions = modStaffOptions.filter(option =>
    option.value !== modLead && option.value !== modModerator
  );

  const Edit = async () => {
    try {
      if (!name || !newModCode) {
        alert("Please fill all required fields");
        return;
      }

      const payload = {
        moduleName: name,
        oldModuleCode: currentModule.moduleCode,
        newModuleCode: newModCode,
        moduleLeadId: Number(modLead),
        moduleModeratorId: Number(modModerator),
        staffIds: modStaff.map(Number)
      };

      console.log("payload send:", payload);

      const response = await axios.post(
        "http://localhost:8080/api/modules/edit",
        payload,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
          }
        }
      );

      console.log("Module edited!:", response.data);
      alert("module edited!");
      navigate("/modules");
    } catch (err) {
      console.error(err);
      alert("Failed to edit module.");
    }
  };

  return (
    <>
      <div className="module-page">
        <div className="module-container">
          <h1 className="title">Editing module: {currentModule?.moduleCode}- {currentModule?.moduleName}</h1>
          <button
            type="button"
            className="back-btn"
            onClick={() => navigate("/modules")}
          >
            Back to modules
          </button>
          <div className="grid-container">
            {/* left area */}
            <div className="left-column">
              <label className="label">Module Code</label>
              <input
                className="input"
                value={newModCode}
                onChange={(e) => setnewModCode(e.target.value)}
                placeholder={currentModule?.moduleCode}
              />

              <label className="label">Module Name</label>
              <input
                className="input"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder={currentModule?.moduleName}
              />

            </div>

            {/* right area */}
            <div className="right-column">
              <label className="label">Module Lead</label>
              <Select
                options={filteredLeadOptions}
                value={
                  (function () {
                    const found = filteredLeadOptions.find(option => option.value === modLead);
                    if (found) {
                      return found;
                    } else {
                      return null;
                    }
                  })()
                }
                onChange={(option) => {
                  if (option) {
                    setModLead(option.value);
                  } else {
                    setModLead("");
                  }
                }}
                className="module-input"
                classNamePrefix="react-select"
                placeholder="Select Lead"
              />

              <label className="label">Module Moderator</label>
              <Select
                options={filteredModeratorOptions}
                value={
                  (function () {
                    const found = filteredModeratorOptions.find(option => option.value === modModerator);
                    if (found) {
                      return found;
                    } else {
                      return null;
                    }
                  })()
                }
                onChange={(option) => {
                  if (option) {
                    setModModerator(option.value);
                  } else {
                    setModModerator("");
                  }
                }}
                className="module-input"
                classNamePrefix="react-select"
                placeholder="Select Moderator"
              />

              <label className="label">Module Staff</label>
              <Select
                isMulti
                options={filteredStaffOptions}
                value={filteredStaffOptions.filter(option => modStaff.includes(option.value))}
                onChange={(selected) => {
                  setModStaff(selected.map(option => option.value));
                }}
                className="module-input"
                classNamePrefix="react-select"
                placeholder="Select module staff"
              />

              <button className="create-btn" onClick={Edit}>
                Update
              </button>
            </div>

          </div>
        </div>
      </div>
      <div className="footer-box">
        <Footer />
      </div>
    </>
  );
}
