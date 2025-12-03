import { useNavigate, useParams } from "react-router-dom";
import { useEffect } from "react";

export default function DeleteModule(){
    const {moduleCode} = useParams();
    const navigate = useNavigate();

    const DeleteModule = async () => {
        const response = await fetch(`http://localhost:8080/api/modules/delete/${moduleCode}`, {
            method: "DELETE"
        })

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