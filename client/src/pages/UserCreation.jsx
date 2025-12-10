import React from "react";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";
import {useNavigate} from "react-router-dom";
import {Navigate} from "react-router-dom";
import {useEffect} from "react";



export default function UserCreation() {

  const navigate = useNavigate();
  const userRole = localStorage.getItem('role');
  const auth = userRole === 'EXAMS_OFFICER' || userRole === 'ADMIN';

  useEffect(() =>{
    if (!auth) {
      navigate("/home", {replace: true});
    }
  }, [navigate]);
  const [selectedRole, setSelectedRole] = React.useState("ADMIN");

return (
  <>
    <Navbar left="COM2008 Systems Design and Security" right="Exam officer"></Navbar>
    {auth ? (
      <div className="login-page">
        <div className="login-card">
          <section className="login-left">
            <div className="login-left-overlay">
              <div className="login-left-header">
              <h2>Create new user</h2>
              </div>
              <div className="login-left-logo">
                  <div className="login-left-logo-placeholder">
                </div>
                <p className="login-left-uni">University of Sheffield</p>
            </div>
            </div>
          </section>

          <section className="login-right">
            <div className="login-right-inner">
              <h1 className="login-title">New user details</h1>
              <form className="login-form">
                <div className="login-field">
                  <label htmlFor="forename">Forename</label>
                  <input
                    id="forename"
                    type="forename"
                    className="login-input"
                    placeholder="forename"
                    />

                  <label htmlFor="surname">Surname</label>
                  <input
                    id="surname"
                    type="surname"
                    className="login-input"
                    placeholder="surname"
                  />

                  <label htmlFor="email">Email</label>
                  <input
                    id="email"
                    type="email"
                    className="login-input"
                    placeholder="name@sheffield.ac.uk"
                  />
                </div>

                <div className="login-field">
                  <label htmlFor="password">Password</label>
                  <input
                    id="password"
                    type="password"
                    className="login-input"
                  />
                </div>
                <label htmlFor="role">Role </label>
                <select value={selectedRole} onChange={(e) => setSelectedRole(e.target.value)}>
                  <option value="ADMIN">Admin</option>
                  <option value="EXAMS_OFFICER">Exams Officer</option>
                  <option value="ACADEMIC_STAFF">Academic Staff</option>
                  </select>
              </form>
            </div>
          </section>
          </div>
      </div>
            
    ) : (
      <p>Access denied {userRole}</p>
    )}
    <div className = "footer-box">
      <Footer/>
    </div>
  </>
  );
}