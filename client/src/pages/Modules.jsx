import React, { useState, useEffect } from "react";
import "./Modules.css";
import "../index.css";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";
import ModuleCard from "../components/ModuleCard.jsx";
import { useNavigate } from "react-router-dom";


function Modules() {
  const [openDropdown, setOpenDropdown] = useState(null);
  const [modules, setModules] = useState([]);
  const navigate = useNavigate();
  if (!localStorage.getItem("token")) { navigate("/login") }

  const GetModules = async () => {
    const role = localStorage.getItem("role");
    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");

    const url =
      role === "ADMIN" || role === "EXAMS_OFFICER"
        ? "http://localhost:8080/api/modules"
        : `http://localhost:8080/api/modules/user/${userId}`;

    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    });

    if (response.ok) {
      const data = await response.json();
      console.log("Modules fetched")
      console.log(data)
      setModules(Array.isArray(data) ? data.filter(m => m.isActive == true) : [])
    } else {
      console.log("Error: Cannot get modules")
    }
  };

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/", { replace: true });
      return;
    }
    GetModules();
  }, [navigate]);


  return (
    <>
      <div className="modules-page">

        <div className="top-container">
          <h2>Assigned Modules</h2>

          {/*Create module admin button */}
          {
            localStorage.getItem("role") === "ADMIN" && (
              <button
                className="create-module-btn"
                onClick={() => window.location.href = "/modules/create"}
              >
                Create Module
              </button>
            )
          }

        </div>

        {/*Module cards */}
        {
          modules.map((mod) => (
            <ModuleCard
              key={mod.moduleCode}
              userId={localStorage.getItem("userId")}
              role={localStorage.getItem("role")}
              module={mod}
              isOpen={openDropdown === mod.moduleCode}
              onToggle={() => setOpenDropdown(openDropdown === mod.moduleCode ? null : mod.moduleCode)}
            />
          ))}
      </div>
      <div className="footer-box">
        <Footer />
      </div>
    </>
  );
}

export default Modules;
