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
      <br/>
      <Link to="/create-new-user">Create new user</Link>
        <br></br>
        <Link to="/modules/create">Create Module</Link>
    </div>
  );
}
