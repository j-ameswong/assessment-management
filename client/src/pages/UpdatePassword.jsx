import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";
import Footer from "../components/Footer.jsx";

function UpdatePassword() {
  const navigate = useNavigate();

  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmNewPassword, setConfirmNewPassword] = useState("");
  const [msg, setMsg] = useState("");
  const [ok, setOk] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg("");
    setOk(false);

    if (!oldPassword || !newPassword || !confirmNewPassword) {
      setMsg("Please fill all fields");
      return;
    }
    if (newPassword === oldPassword) {
      setMsg("New password must be different");
      return;
    }
    if (newPassword !== confirmNewPassword) {
      setMsg("New passwords do not match");
      return;
    }
    if (newPassword.length < 8) {
      setMsg("New password must be at least 8 characters");
      return;
    }

    try {
      setLoading(true);
      const token = localStorage.getItem("token");
      const res = await fetch("http://localhost:8080/api/auth/update-password", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token || ""}`,
        },
        body: JSON.stringify({ oldPassword, newPassword }),
      });

      if (res.status === 401) {
        setMsg("Old password is incorrect");
        setOk(false);
        return;
      }
      if (!res.ok) {
        const text = await res.text().catch(() => "");
        setMsg(text || "Failed to update password");
        setOk(false);
        return;
      }

      setOk(true);
      setMsg("Password updated. Redirecting...");
      setTimeout(() => {
        navigate("/modules");
      }, 800);
    } catch (err) {
      setMsg("Cannot connect to server");
      setOk(false);
    } finally {
      setLoading(false);
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
              <div className="login-left-logo-placeholder">Logo</div>
              <p className="login-left-uni">University of Sheffield</p>
            </div>
          </div>
        </section>

        <section className="login-right">
          <div className="login-right-inner">
            <h1 className="login-title">Update password</h1>

            <form className="login-form" onSubmit={handleSubmit}>
              <div className="login-field">
                <label htmlFor="oldPassword">Old password</label>
                <input
                  id="oldPassword"
                  type="password"
                  className="login-input"
                  value={oldPassword}
                  onChange={(e) => setOldPassword(e.target.value)}
                  required
                />
              </div>

              <div className="login-field">
                <label htmlFor="newPassword">New password</label>
                <input
                  id="newPassword"
                  type="password"
                  className="login-input"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  required
                />
              </div>

              <div className="login-field">
                <label htmlFor="confirmNewPassword">Confirm new password</label>
                <input
                  id="confirmNewPassword"
                  type="password"
                  className="login-input"
                  value={confirmNewPassword}
                  onChange={(e) => setConfirmNewPassword(e.target.value)}
                  required
                />
              </div>

              <button type="submit" className="login-submit" disabled={loading}>
                {loading ? "Submitting..." : "Submit"}
              </button>

              {msg && (
                <p className={`login-note ${ok ? "up-ok" : "up-err"}`}>{msg}</p>
              )}
            </form>
          </div>
        </section>
      </div>
      <Footer />
    </div>
  );
}

export default UpdatePassword;