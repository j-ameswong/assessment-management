import React from "react";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";

export default function UserCreation(){

  const userRole = localStorage.getItem('userRole');
  const auth = userRole === 'EXAMS_OFFICER' || userRole === 'ADMIN';

return (
  <>
    <Navbar left="COM2008 Systems Design and Security" right="Exam officer"></Navbar>
    {auth ? (
      <p>Access granted {userRole}</p>
    ) : (
      <p>Access denied {userRole}</p>
    )}
    <div className = "footer-box">
      <Footer/>
    </div>
  </>
  );
}