import React, {useState} from "react";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

export default function RoleManagement () {

  //const navigate = useNavigate();
  const userRole = localStorage.getItem('role');
  const auth = userRole === 'EXAMS_OFFICER';
  const [message, setMessage] = useState("");
  const [names, setNames] = useState([]);

  const fetchNames = async () => {
        try {
          const response = await fetch("http://localhost:8080/api/users", {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`
            }
          });
          if (response.ok) {
            const data = await response.json();
            const filter = data.filter(user =>
              user.role === 'EXAMS_OFFICER' || user.role === 'ADMIN'
            );
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
        body: JSON.stringify(newRole)
      });

      if (response.ok) {
        fetchNames();
      }

    } catch {
      setMessage("Failed to update role");
    }
  }

    //useEffect(() =>{
    //if (!auth) {
    //navigate("/home", {replace: true});
    //}
    //}, [navigate]);

    useEffect(() => {
      fetchNames();
    }, []);

    return (
      <>
        <Navbar left="COM2008 Systems Design and Security" right={userRole}></Navbar>
        <p>{message} </p>
        {auth ? (
          <>
            <ul>
              {names.map((user) => (
                <li>{user.forename} {user.surname} - {user.role}
                  <button onClick={() => handleRoleChange(user.id, user.role)}>
                    Change Role
                  </button>
                </li>
              ))}
            </ul>
          </>
        ) : (<></>
        )}
        <div className="footer-box">
          <Footer/>
        </div>
      </>
    )
}