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