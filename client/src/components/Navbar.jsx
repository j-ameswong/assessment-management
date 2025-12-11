import { useContext } from "react";
import { UserContext } from "../context/UserContext";
import { Link } from "react-router-dom";
import "./Navbar.css";

export default function Navbar() {

  const { user } = useContext(UserContext);
  const primaryRole = user?.role;

  console.log("Navbar user:", user);
  if (localStorage.getItem("token")) {
    return (
      <header className="navbar">
        <div className="navbar-left">
          {user && (
            <>
              <span className="navbar-welcome">
                Welcome, {user.forename} {user.surname}
              </span>

              <Link className="navbar-link" to="/modules">
                Modules
              </Link>

              {(primaryRole === "ADMIN" || primaryRole === "EXAMS_OFFICER") &&
                <Link className="navbar-link" to="/modules/all/assessments">
                  Assessments
                </Link>
              }
            </>
          )}
        </div>

        <div className="navbar-right">
          {primaryRole && (
            <>
              <span className="navbar-role">
                {primaryRole.replaceAll("_", " ")}
              </span>

              <Link className="navbar-link" to="/my-tasks">
                My tasks
              </Link>

              {(primaryRole === "ADMIN" || primaryRole === "EXAMS_OFFICER") && (
                <div className="navbar-dropdown">
                  <button className="navbar-dropbtn">Admin</button>
                  <div className="navbar-dropdown-content">
                    <Link
                      className="navbar-dropdown-item"
                      to="/modules/create"
                    >
                      Create module
                    </Link>
                    <Link
                      className="navbar-dropdown-item"
                      to="/create-new-user"
                    >
                      Create user
                    </Link>
                    <Link
                      className="navbar-dropdown-item"
                      to="/users/delete"
                    >
                      Manage users
                    </Link>
                  </div>
                </div>
              )}

              <Link className="navbar-logout" to="/logout">
                Logout
              </Link>
            </>
          )}
        </div>

      </header>
    );
  }
}
