import { useContext } from "react";
import { UserContext } from "../context/UserContext";
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
          <span className="navbar-role">
            {primaryRole.replaceAll("_", " ")}
          </span>
        )}
      </div>
    </header>
  );
}
