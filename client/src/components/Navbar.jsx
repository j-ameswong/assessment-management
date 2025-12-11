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

            <Link className="navbar-link" to="/my-tasks">
              My tasks
            </Link>

            {(primaryRole === "ADMIN" || primaryRole === "EXAMS_OFFICER") && (
              <Link className="navbar-link" to="/users/delete">
                Manage users
              </Link>
            )}

            {/* 新增的 Logout 按钮 */}
            <Link className="navbar-logout" to="/logout">
              Logout
            </Link>
          </>
        )}
      </div>
    </header>
  );
}
