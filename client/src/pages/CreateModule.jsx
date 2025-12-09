
import {useEffect, useState} from "react";
import axios from "axios";
import "./CreateModule.css";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";

export default function CreateModule() {
    // Module Fields
    const [name, setName] = useState("");
    const [modCode, setModCode] = useState("");
    const [modLead, setModLead] = useState("");
    const [modModerator, setModModerator] = useState("");
    const [modStaff, setModStaff] = useState([]);
    const [csvFile, setCsvFile] = useState(null);

    // Fetch users
    const [staffList, setStaffList] = useState([]);
    useEffect(() => {
        fetch("http://localhost:8080/api/users")
            .then(res => {
                console.log("Status code: ", res.status); // for debugging
                return res.json();
            })
            .then(data => {
                console.log("Fetched users: ", data); // for debugging
                setStaffList(data);
            })
            .catch(err => console.error("Error loading staff members: ", err));
    }, []);

    const Create = async () => {
        try {
            if (!name || !modCode || !modLead || !modModerator) {
                alert("Please fill all required fields");
                return;
            }

            const payload = {
                moduleName: name,
                moduleCode: modCode,
                moduleLeadId: Number(modLead),
                moduleModeratorId: Number(modModerator),
                staffIds: modStaff.map(Number)
            };

            console.log("payload send:", payload);

            const response = await axios.post(
                "http://localhost:8080/api/modules",
                payload
            );

            console.log("Created:", response.data);
            alert("module created!");
        } catch (err) {
            console.error(err);
            alert("Failed to create module.");
        }
    };

    const CsvUpload = async () => {
        const formData = new FormData();
        formData.append("file", csvFile);

        await axios.post(
            "http://localhost:8080/api/modules/uploadCsv",
            formData,
            {
                headers: { "Content-Type": "multipart/form-data" },
            }
        );

        alert("CSV uploaded!");
    };

    return (
        <>
            <Navbar left="Create new module" right="Role" />
            <div className="module-page">
                <div className="module-container">
                    <h1 className="title">Create New Module</h1>
                    <div className="grid-container">
                        {/* left area */}
                        <div className="left-column">
                            <label className="label">Module Name</label>
                            <input
                                className="input"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                            />

                            <label className="label">Module Code</label>
                            <input
                                className="input"
                                value={modCode}
                                onChange={(e) => setModCode(e.target.value)}
                            />

                            <div className="or">
                                <p>OR</p>
                            </div>

                            <label className="label">Upload .csv file</label>
                            <div className="csv-wrapper">
                                <input
                                    className="csv-input"
                                    type="file"
                                    accept=".csv"
                                    onChange={(e) => setCsvFile(e.target.files[0])}
                                />

                                <div className="file-display">
                                    <img
                                        src="/icons/file.svg"
                                        alt="file upload icon"
                                        className="file-icon"
                                    />
                                </div>

                                <button className="attach-btn" onClick={CsvUpload}>
                                    Attach
                                </button>
                            </div>
                        </div>

                        {/* right area */}
                        <div className="right-column">
                            <label className="label">Module Lead</label>
                            <select
                                id="lead"
                                value={modLead}
                                onChange={(e) => setModLead(e.target.value)}
                                className="module-input"
                            >
                                <option value="">Select Lead</option>
                                {staffList.map(user => (
                                    <option key={user.id} value={user.id}>
                                        {`${user.forename} ${user.surname}`}
                                    </option>
                                ))}
                            </select>

                            <label className="label">Module Moderator</label>
                            <select
                                id="moderator"
                                value={modModerator}
                                onChange={(e) => setModModerator(e.target.value)}
                                className="module-input"
                            >
                                <option value="">Select Moderator</option>
                                {staffList.map(user => (
                                    <option key={user.id} value={user.id}>
                                        {`${user.forename} ${user.surname}`}
                                    </option>
                                ))}
                            </select>

                            <label className="label">Module Staff</label>
                            <select
                                id="otherStaff"
                                multiple
                                value={modStaff}
                                onChange={(e) =>
                                    setModStaff(
                                        Array.from(e.target.selectedOptions, opt => opt.value)
                                    )
                                }
                                className="module-input"
                            >
                                {staffList.map(user => (
                                    <option key={user.id} value={user.id}>
                                        {`${user.forename} ${user.surname}`}
                                    </option>
                                ))}
                            </select>

                            <button className="create-btn" onClick={Create}>
                                Create
                            </button>
                        </div>

                    </div>
                </div>
            </div>
            <div className="footer-box">
                    <Footer/>
            </div>
        </>
    );
}