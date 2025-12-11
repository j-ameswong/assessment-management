import { useContext } from "react";
import { UserContext } from "../context/UserContext";
import { Link } from "react-router-dom";
import "./Navbar.css";

export default function Navbar() {
  const { user } = useContext(UserContext);
  const primaryRole = user?.role;

  console.log("Navbar user:", user);

  return (
    <header className="navbar">
      <div className="navbar-left">
        {user && (
          <span className="navbar-welcome">
            Welcome, {user.forename} {user.surname}
          </span>
        )}
      </div>

      <div className="navbar-right">
        {primaryRole && (
          <>
            <span className="navbar-role">
              {primaryRole.replaceAll("_", " ")}
            </span>

            {(primaryRole === "ADMIN" || primaryRole === "EXAMS_OFFICER") && (
              <Link className="navbar-link" to="/users/delete">
                Manage users
              </Link>
            )}
          </>
        )}
      </div>

    </header>
  );
}
