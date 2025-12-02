import React from "react";
import { Link } from "react-router-dom";
import "./Modules.css";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";
import ModuleCard from "../components/ModuleCard.jsx";

function Modules() {
    return(
        <>
            <Navbar left="Modules" right="Role"/>
            <div className="modules-page">
                <ModuleCard moduleCode={"COM1001"} moduleTitle={"Introduction to software engineering"}/>
                <ModuleCard moduleCode={"COM1003"} moduleTitle={"Java Programming"}/>
                
            </div>
            <div className="footer-box">
                <Footer/>
            </div>
        </>
    );
}

export default Modules;