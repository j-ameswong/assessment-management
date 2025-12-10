import React, {useState} from "react";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";


export default function UserCreation() {

  const navigate = useNavigate();
  const userRole = localStorage.getItem('role');
  const auth = userRole === 'EXAMS_OFFICER' || userRole === 'ADMIN';
  const [message, setMessage] = useState("");

  useEffect(() =>{
    if (!auth) {
      navigate("/home", {replace: true});
    }
  }, [navigate]);

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [forename, setForename] = useState("");
  const [surname, setSurname] = useState("");
  const [selectedRole, setSelectedRole] = useState("ADMIN");

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/api/users", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${localStorage.getItem("token")}`
        },
        body: JSON.stringify({forename, surname, email, password, role: selectedRole})
      });

      if (!response.ok) {
        setMessage("Failed to create user");
      } else {
        setMessage("User created successfully");
        navigate("/create-new-user", {replace: true});
        setSurname("");
        setForename("");
        setEmail("");
        setPassword("");
      }
    } catch (error) {
      console.error(error);
      setMessage("Cannot connect to server");
    }
  }

return (
  <>
    <Navbar left="COM2008 Systems Design and Security" right={userRole}></Navbar>
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
              <form className="login-form" onSubmit={handleSubmit}>
                <div className="login-field">
                  <label htmlFor="forename">Forename</label>
                  <input
                    id="forename"
                    type="forename"
                    className="login-input"
                    placeholder="forename"
                    value = {forename}
                    onChange={(e) => setForename(e.target.value)}
                    required
                    />

                  <label htmlFor="surname">Surname</label>
                  <input
                    id="surname"
                    type="surname"
                    className="login-input"
                    placeholder="surname"
                    value = {surname}
                    onChange={(e) => setSurname(e.target.value)}
                    required
                  />

                  <label htmlFor="email">Email</label>
                  <input
                    id="email"
                    type="email"
                    className="login-input"
                    placeholder="name@sheffield.ac.uk"
                    value = {email}
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
                    placeholder="password"
                    value = {password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                </div>
                <label htmlFor="role">Role </label>
                <select value={selectedRole} onChange={(e) => setSelectedRole(e.target.value)}>
                  <option value="ADMIN">Admin</option>
                  <option value="ACADEMIC_STAFF">Academic Staff</option>
                  <option value="EXTERNAL_EXAMINER">External Examiner</option>
                  </select>

                <button type="submit" className="login-submit">
                  Submit
                </button>
                <p>{message}</p>

              </form>
            </div>
          </section>
          </div>
      </div>
            
    ) : (<></>
    )}
    <div className = "footer-box">
      <Footer/>
    </div>
  </>
  );
}