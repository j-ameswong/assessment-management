import React, { useState, useEffect } from "react";
import "./Modules.css";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";
import ModuleCard from "../components/ModuleCard.jsx";


function Modules() {
    const [openDropdown, setOpenDropdown] = useState(null);
    const [modules, setModules] = useState([]);


    const GetModules = async () => {
        const role = localStorage.getItem("role");
        const userId = localStorage.getItem("userId");
        const token = localStorage.getItem("token");

        const url =
            role === "ADMIN" || role === "EXAM_OFFICER"
                ? "http://localhost:8080/api/modules"
                : `http://localhost:8080/api/modules?userId=${userId}`;

        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            setModules(data);
        } else {
            console.log("Error: Cannot get modules")
        }
    };

    useEffect (() => {
        GetModules();
    }, [])

    return(
        <>
            <Navbar left="Modules" right="Role"/>
            <div className="modules-page">

                {
                    modules.map((mod) => (
                        <ModuleCard
                            key={mod.moduleCode}
                            moduleCode={mod.moduleCode}
                            moduleTitle={mod.moduleName}
                            isOpen={openDropdown === mod.moduleCode}
                            onToggle={() => setOpenDropdown(openDropdown === mod.moduleCode ? null : mod.moduleCode)}
                        />
                ))}
            </div>
            <div className="footer-box">
                <Footer/>
            </div>
        </>
    );
}

export default Modules;