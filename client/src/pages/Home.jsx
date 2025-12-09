import { Link } from "react-router-dom";

console.log(localStorage);
export default function Home() {

  return (
    <div>
      <h1>Home Page</h1>
      <Link to="/modules/:moduleId/assessments/new">Create Assessment</Link>
      <br></br>
      <Link to="/modules/1/assessments">View Module 1 Assessments</Link>
      <br></br>
      <Link to="/modules">Modules</Link>
    </div>
  );
}
