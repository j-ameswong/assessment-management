import React from 'react';
import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Home from "./pages/Home.jsx";
import Modules from './pages/Modules.jsx';
import CreateAssessment from "./pages/CreateAssessment.jsx";
import AssessmentProgression from "./pages/AssessmentProgression.jsx";
import AssessmentOverview from "./pages/AssessmentOverview.jsx";
import CreateModule from './pages/CreateModule.jsx';
import DeleteModule from './pages/DeleteModule.jsx';
import UserCreation from './pages/UserCreation.jsx';
import Logout from './pages/Logout.jsx';
import EditModule from './pages/EditModule.jsx';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/home" element={<Home />} />
      <Route path="/modules" element={<Modules />} />
      <Route path="/modules/create" element={<CreateModule />} />
      <Route path="/modules/edit" element={<EditModule />} />
      <Route path="/modules/:moduleId/assessments/new" element={<CreateAssessment />} />
      <Route path="/modules/:moduleId/assessments" element={<AssessmentOverview />} />
      <Route path="/modules/:moduleId/assessments/:assessmentId/progress" element={<AssessmentProgression />} />
      <Route path="/create-new-user" element={<UserCreation />} />
      <Route path="/logout" element={<Logout />} />
    </Routes>
  );
}

export default App;
