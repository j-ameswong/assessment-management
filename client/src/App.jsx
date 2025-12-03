import React from 'react';
import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Home from "./pages/Home.jsx";
import Modules from './pages/Modules.jsx';
import CreateAssessment from "./pages/CreateAssessment.jsx";
import AssessmentOverview from "./pages/AssessmentOverview.jsx";
import CreateModule from './pages/CreateModule.jsx';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/home" element={<Home />} />
      <Route path="/modules" element={<Modules />} />
      <Route path="/modules/create" element={<CreateModule />} />
      <Route path="/modules/:moduleId/assessments/new" element={<CreateAssessment />} />
      <Route path="/modules/:moduleId/assessments" element={<AssessmentOverview />} />
    </Routes>
  );
}


export default App;
