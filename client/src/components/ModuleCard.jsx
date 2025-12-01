import React from "react";
import { useState } from 'react';
import "../componentStyles/ModuleCard.css"

export default function ModuleCard({ moduleCode, moduleTitle }) {
    const [ddOpen, setDdOpen] = useState(false);

    const toggleDropdown = () => {
        setDdOpen(prev => !prev)
    };

    return (
        <div className="module-box">
            <h2 className="module-code">{moduleCode}</h2>
            <div className="module-info">
                <div className="module-title-dropdown-row">
                    <h2 className="module-title">{moduleTitle}</h2>
                    <div className="dropdown">
                        <button className="dropdown-btn" onClick={toggleDropdown}>
                            Dropdown
                        </button>
                        { ddOpen && (
                            <div className="dropdown-content">
                                <a href="#">Go to module</a>
                                <a href="#">Edit module</a>
                                <a href="#">Delete module</a>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}