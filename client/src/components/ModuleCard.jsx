import React from "react";
import { useState } from 'react';

const ModuleCard = (props) => {
    const [ddOpen, setDdOpen] = useState(false);

    const toggleDropdown = () => {
        setDdOpen(prev => !prev)
    };

    return (
        <div className="module-box">
            <h2 className="module-code">{props.moduleCode}</h2>
            <div className="module-info">
                <h2 className="module-title">{props.moduleTitle}</h2>
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
    );
};

export default ModuleCard