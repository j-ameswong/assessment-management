import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./Modules.css";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";
import ModuleCard from "../components/ModuleCard.jsx";

function Modules() {
    const [openDropdown, setOpenDropdown] = useState(null);
    const modules = [
        { moduleCode: "COM1001", moduleTitle: "Introduction to software engineering" },
        { moduleCode: "COM1003", moduleTitle: "Java Programming" }
    ];
    return(
        <>
            <Navbar left="Modules" right="Role"/>
            <div className="modules-page">
                {modules.map((mod) => (
                    <ModuleCard
                        key={mod.moduleCode}
                        moduleCode={mod.moduleCode}
                        moduleTitle={mod.moduleTitle}
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