import React, { useState } from "react";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import "./RoleManagement.css";

export default function RoleManagement() {

  const navigate = useNavigate();
  if (!localStorage.getItem("token")) { navigate("/login") }
  const userRole = localStorage.getItem('role');
  const auth = userRole === 'EXAMS_OFFICER'; //ensures the user accessing the page is an exam officer
  const [message, setMessage] = useState("");
  const [names, setNames] = useState([]);

  const fetchNames = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/users", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        } //fetches the users from the database
      });
      if (response.ok) {
        const data = await response.json();
        const currentUser = localStorage.getItem("userId");
        const filter = data.filter(user =>
          (user.role === 'EXAMS_OFFICER' || user.role === 'ADMIN') && user.id != currentUser
        ); //selects all of the exam officers and admins excluding the user viewing the page
        setNames(filter);
      } else {
        setMessage("Failed to fetch users");
      }
    } catch {
      setMessage("Cannot connect to database");
    }
  };

  const handleRoleChange = async (id, currentRole) => {
    const newRole = currentRole === 'ADMIN' ? 'EXAMS_OFFICER' : 'ADMIN';

    try {
      const response = await fetch(`http://localhost:8080/api/users/${id}/role`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${localStorage.getItem("token")}`
        },
        body: JSON.stringify(newRole) //changes the role for the selected user
      });

      if (response.ok) {
        fetchNames(); //updates the page to display new role
      }

    } catch {
      setMessage("Failed to update role");
    }
  }

  useEffect(() => {
    if (!auth) {
      navigate("/users/delete", { replace: true });
    }
  }, [navigate]); //sends the user to home if they arent an exams officer

  useEffect(() => {
    fetchNames();
  }, []);

  return (
    <>
      <Navbar left="COM2008 Systems Design and Security" right={userRole}></Navbar>
      <p>{message} </p>
      {auth ? (
        <>
          <div className="page">
            <h1>Role management</h1>
            <div className="table-wrapper">
              <table class="table">
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Role</th>
                    <th style={{ width: "110px" }}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {names.map((user) => (
                    <tr>
                      <td>{user.forename} {user.surname} </td>
                      <td>{user.role}</td>
                      <td><button onClick={() => handleRoleChange(user.id, user.role)}>
                        Change Role
                      </button></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </>
      ) : (<></>
      )}
      <div className="footer-box">
        <Footer />
      </div>
    </>
  )
}
