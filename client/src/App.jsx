import React from 'react';
import { Routes, Route, useLocation } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Home from "./pages/Home.jsx";
import Modules from './pages/Modules.jsx';
import CreateAssessment from "./pages/CreateAssessment.jsx";
import AssessmentProgression from "./pages/AssessmentProgression.jsx";
import AssessmentOverview from "./pages/AssessmentOverview.jsx";
import CreateModule from './pages/CreateModule.jsx';
import UserCreation from './pages/UserCreation.jsx';
import Logout from './pages/Logout.jsx';
import EditModule from './pages/EditModule.jsx';
import UpdatePassword from "./pages/UpdatePassword.jsx";
import Layout from "./components/Layout.jsx";
import DeleteModule from './pages/DeleteModule.jsx';
import AssessmentLogs from "./pages/AssessmentLogs.jsx";
import UserDeletion from "./pages/UserDeletion.jsx";
import MyTasks from "./pages/MyTasks.jsx";
import RoleManagement from "./pages/RoleManagement.jsx";

function App() {
  const location = useLocation();
  // if (hideNavbar) {
  //   return (
  //     <Routes>
  //       <Route path="/" element={<Login />} />
  //       <Route path="/create-new-user" element={<UserCreation />} />
  //     </Routes>
  //   );
  // }
  //
  return (
    <Layout>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/modules" element={<Modules />} />
        <Route path="/modules/create" element={<CreateModule />} />
        <Route path="/modules/edit/:moduleCode" element={<EditModule />} />
        <Route path="/modules/delete/:moduleCode" element={<DeleteModule />} />
        <Route path="/modules/:moduleId/assessments/new" element={<CreateAssessment />} />
        <Route path="/modules/:moduleId/assessments" element={<AssessmentOverview />} />
        <Route path="/modules/all/assessments" element={<AssessmentOverview />} />
        <Route path="/modules/:moduleId/assessments/:assessmentId/progress" element={<AssessmentProgression />} />
        <Route
          path="/modules/:moduleId/assessments/:assessmentId/logs"
          element={<AssessmentLogs />}
        />
        <Route path="/create-new-user" element={<UserCreation />} />
        <Route path="/logout" element={<Logout />} />
        <Route path="/update-password" element={<UpdatePassword />} />
        <Route path="/users/delete" element={<UserDeletion />} />
        <Route path="/my-tasks" element={<MyTasks />} />
        <Route path="/role-management" element={<RoleManagement />} />
      </Routes>
    </Layout>
  );
}

export default App;
