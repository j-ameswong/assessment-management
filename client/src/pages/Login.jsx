import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Login.css";
import Footer from "../components/Footer.jsx";


function Login() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [, setMessage] = useState("");

  const handleSubmit = async (event) => {
    event.preventDefault();
    console.log("Login form submitted");

    try {
      let username = email;
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
      });

      if (!response.ok) {
        setMessage("Invalid email or password");
      } else {
        const data = await response.json();
        localStorage.setItem("token", data.token);
        localStorage.setItem("userId", data.id);
        localStorage.setItem("role", data.role);

        setMessage("Login successful");
        // check if the account must change password on first login
        const meRes = await fetch("http://localhost:8080/api/auth/me", {
          headers: { Authorization: `Bearer ${data.token}` }
        });
        if (meRes.ok) {
          const me = await meRes.json();
          localStorage.setItem("mustChangePassword", String(!!me.mustChangePassword));
          if (me.mustChangePassword) {
            navigate("/update-password");
            return; // stop here so we do not go to /home
          }
        }

        navigate("/modules");
      }

    } catch (error) {
      console.error(error);
      setMessage("Cannot connect to server");
    }
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
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>

              <div className="login-field">
                <label htmlFor="password">Password</label>
                <input
                  id="password"
                  type="password"
                  className="login-input"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>

              <button type="submit" className="login-submit">
                Submit
              </button>

              <p className="login-note">
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