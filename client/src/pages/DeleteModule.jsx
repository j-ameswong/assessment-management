import { useNavigate, useParams } from "react-router-dom";
import { useEffect } from "react";
import axios from "axios";

export default function DeleteModule() {
  const { moduleCode } = useParams();
  const navigate = useNavigate();

  if (!localStorage.getItem("token")) { navigate("/login") }

  const handleDelete = async () => {
    const response = await axios.delete(
      `http://localhost:8080/api/modules/delete/${moduleCode}`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
          "Content-Type": "application/json"
        }
      }
    );

    if (response.status == 200) {
      navigate("/modules")
    } else {
      console.log("Failed to delete module")
    }
  };

  useEffect(() => {
    handleDelete();
  }, [moduleCode, navigate]);

  return <div>Deleting Module</div>
}
