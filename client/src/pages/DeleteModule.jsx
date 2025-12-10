import { useNavigate, useParams } from "react-router-dom";
import { useEffect } from "react";
import axios from "axios";

export default function DeleteModule(){
    const {moduleCode} = useParams();
    const navigate = useNavigate();

    const DeleteModule = async () => {
        const response = await axios.delete(
                `http://localhost:8080/api/modules/delete/${moduleCode}`,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                        "Content-Type": "application/json"
                    }
                }
            );

        if(response.ok){
            navigate("/modules")
        } else {
            console.log("Failed to delete module")
        }
    };

    useEffect(() => {
        DeleteModule();
    }, [moduleCode, navigate]);

    return <div>Deleting Module</div>
}