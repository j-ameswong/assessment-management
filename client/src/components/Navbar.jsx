import { useContext } from "react";
import { UserContext } from "../context/UserContext";
import "./Navbar.css";

export default function Navbar() {
  const { user } = useContext(UserContext);

  console.log("Navbar user:", user);

  const primaryRole = user?.role; // 后端返回的是 user.role

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
          <span className="navbar-role">
            {primaryRole.replaceAll("_", " ")}
          </span>
        )}
      </div>
    </header>
  );
}
