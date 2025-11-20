import React from "react";
import { Link } from "react-router-dom";
import "./Login.css";
import Footer from "../components/Footer.jsx";


function Login() {
  const handleSubmit = (event) => {
    event.preventDefault();
    // Real login logic will be added
    console.log("Login form submitted");
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <section className="login-left">
          <div className="login-left-overlay">
            <div className="login-left-header">
              <h2>Assessment Management Tool</h2>
            </div>

            <div className="login-left-logo">
            <div className="login-left-logo-placeholder">
                Logo
            </div>
            <p className="login-left-uni">University of Sheffield</p>
            </div>

          </div>
        </section>

        <section className="login-right">
          <div className="login-right-inner">
            <h1 className="login-title">Sign in</h1>

            <form className="login-form" onSubmit={handleSubmit}>
              <div className="login-field">
                <label htmlFor="email">Email</label>
                <input
                  id="email"
                  type="email"
                  className="login-input"
                  placeholder="name@sheffield.ac.uk"
                  required
                />
              </div>

              <div className="login-field">
                <label htmlFor="password">Password</label>
                <input
                  id="password"
                  type="password"
                  className="login-input"
                  required
                />
              </div>

              <button type="submit" className="login-submit">
                Submit
              </button>

              <p className="login-note">
                  Don't have an account?{" "}
                    <Link to="/register" className="login-register-link">
                        <em>Click here to register.</em>
                    </Link>
              </p>
            </form>
          </div>
        </section>
        
        
      </div>
       <Footer />
    </div>
    
  );
}

export default Login;
