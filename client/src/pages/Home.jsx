import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

export default function Home() {
    const [message, setMessage] = useState("");

    useEffect(() => {
        fetch("http://localhost:8080/api/auth/test")
            .then(res => res.text())
            .then(data => setMessage(data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div>
            <h1>Home Page</h1>
            <p>{message}</p>
            <Link to="/add-assessment">Create Assessment</Link>
            <br></br>
            <Link to="/modules/assessments/1">View Module 1 Assessments</Link>
        </div>
    );
}
