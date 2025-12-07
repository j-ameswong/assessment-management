import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

console.log(localStorage);
export default function Home() {
  const [message, setMessage] = useState("");

    useEffect(() => {
      const token = localStorage.getItem("token");

      fetch("http://localhost:8080/api/auth/test", {
        headers: {
            "Authorization": `Bearer ${token}`
        }
      })
        .then(res => res.text())
        .then(data => setMessage(data))
        .catch(err => console.error(err));
    }, []);

  return (
    <div>
      <h1>Home Page</h1>
      <p>{message}</p>
      <Link to="/modules/:moduleId/assessments/new">Create Assessment</Link>
      <br></br>
      <Link to="/modules/1/assessments">View Module 1 Assessments</Link>
      <br></br>
      <Link to="/modules">Modules</Link>
    </div>
  );
}
