import React from 'react';
import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Home from "./pages/Home.jsx";
import CreateAssessment from "./pages/CreateAssessment.jsx";

function App() {
    return (
        <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/home" element={<Home />} />
            <Route path="/add-assessment" element={<CreateAssessment />} />
        </Routes>
    );
}

export default App;
