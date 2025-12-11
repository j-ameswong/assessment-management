import React, { useEffect, useState } from "react";
import {Link, useNavigate} from "react-router-dom";
import api from "../api/axiosConfig";
import "./UserDeletion.css";

export default function UserDeletion() {
  const navigate = useNavigate();

  const role = localStorage.getItem("role");
  const canManageUsers = role === "ADMIN" || role === "EXAMS_OFFICER";

  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [deletingId, setDeletingId] = useState(null);

  useEffect(() => {
    if (!localStorage.getItem("token")) {
      navigate("/", { replace: true });
      return;
    }

    if (!canManageUsers) {
      navigate("/modules", { replace: true });
      return;
    }

    const fetchUsers = async () => {
      try {
        const response = await api.get("/users");
        setUsers(response.data || []);
      } catch (err) {
        console.error("Failed to load users:", err);
        setError("Failed to load users.");
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, [navigate, canManageUsers]);

  const examsOfficers = users.filter((u) => u.role === "EXAMS_OFFICER");

  const handleDelete = async (user) => {
    if (user.role === "EXAMS_OFFICER") {
      window.alert(
        "You cannot delete an Exams Officer directly.\n" +
          "Please assign a new Exams Officer and demote this user first."
      );
      return;
    }

    const confirmed = window.confirm(
      `Are you sure you want to delete ${user.forename} ${user.surname}?`
    );
    if (!confirmed) return;

    try {
      setDeletingId(user.id);
      await api.delete(`/users/${user.id}`);

      setUsers((prev) => prev.filter((u) => u.id !== user.id));
    } catch (err) {
      console.error("Failed to delete user:", err);

      if (err.response?.status === 409) {
        window.alert(
          "The server reported that this user cannot be deleted.\n" +
            "Most likely they are still an Exams Officer.\n" +
            "Please assign a new Exams Officer and demote this user first."
        );
      } else if (err.response?.status === 403) {
        window.alert("You do not have permission to delete users.");
      } else if (err.response?.status === 404) {
        window.alert("User not found (it may already have been deleted).");
      } else {
        window.alert("Failed to delete user. Please try again.");
      }
    } finally {
      setDeletingId(null);
    }
  };

  if (!canManageUsers) {
    return <p>Access denied.</p>;
  }

  if (loading) {
    return <p className="user-delete-loading">Loading users...</p>;
  }

  if (error) {
    return <p className="user-delete-error">{error}</p>;
  }

  return (
    <div className="user-delete-page">
      <div className="user-delete-header">
        <Link to="/create-new-user">Create new user</Link>
        <br/>
        <Link to="/role-management">Manage Roles</Link>
        <h2>User Deletion</h2>
        <p>
          Deleting a user <strong>disables their account</strong> but keeps all
          assessment tracking data, so history still shows who performed each
          action.
        </p>
      </div>

      <div className="user-delete-rules">
        <h3>Exams Officer rule</h3>
        <p>
          Exams Officers <strong>cannot be deleted directly</strong>. To remove
          an Exams Officer account:
        </p>
        <ol>
          <li>Assign a new Exams Officer by changing another user&apos;s role.</li>
          <li>Demote the old Exams Officer to a different role.</li>
          <li>Return to this page and delete the old account.</li>
        </ol>

        {examsOfficers.length > 0 && (
          <p className="user-delete-current-exams">
            Current Exams Officer
            {examsOfficers.length > 1 ? "s" : ""}:{" "}
            {examsOfficers
              .map((u) => `${u.forename} ${u.surname}`)
              .join(", ")}
          </p>
        )}

        <p className="user-delete-note">
          Role changes (assigning the new Exams Officer / demoting the old one)
          are handled on the <em>role management</em> screens. This page
          only controls deletion.
        </p>
      </div>

      {users.length === 0 ? (
        <p>No users found.</p>
      ) : (
        <div className="user-delete-table-wrapper">
          <table className="user-delete-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th style={{ width: "110px" }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id}>
                  <td>
                    {user.forename} {user.surname}
                  </td>
                  <td>{user.email}</td>
                  <td>{user.role?.replaceAll("_", " ")}</td>
                  <td>
                    <button
                      className="user-delete-btn"
                      onClick={() => handleDelete(user)}
                      disabled={deletingId === user.id}
                    >
                      {deletingId === user.id ? "Deleting..." : "Delete"}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
