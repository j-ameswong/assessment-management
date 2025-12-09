
import {useEffect, useState} from "react";
import Select from "react-select";
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
        fetch("http://localhost:8080/api/users", {
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "application/json"
            }
        })
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

    // Staff
    const modStaffOptions = staffList.map(user => ({
        value: user.id,
        label: `${user.forename} ${user.surname}`
    }));

    // Filtered options for selection
    const filteredLeadOptions = modStaffOptions.filter(option =>
        option.value !== modModerator && !modStaff.includes(option.value)
    )
    const filteredModeratorOptions = modStaffOptions.filter(option =>
        option.value !== modLead && !modStaff.includes(option.value)
    );
    const filteredStaffOptions = modStaffOptions.filter(option =>
        option.value !== modLead && option.value !== modModerator
    );

    // For debugging : see what users have been selected
    console.log("Selected lead:", modLead);
    console.log("Selected moderator:", modModerator);
    console.log("Selected module staff:", modStaff);

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
                            <Select
                                options={filteredLeadOptions}
                                value={
                                    (function() {
                                        const found = filteredLeadOptions.find(option => option.value === modLead);
                                        if (found) {
                                            return found;
                                        } else {
                                            return null;
                                        }
                                    })()
                                }
                                onChange={(option) => {
                                    if (option) {
                                        setModLead(option.value);
                                    } else {
                                        setModLead("");
                                    }
                                }}
                                className="module-input"
                                classNamePrefix="react-select"
                                placeholder="Select Lead"
                            />

                            <label className="label">Module Moderator</label>
                            <Select
                                options={filteredModeratorOptions}
                                value={
                                    (function() {
                                        const found = filteredModeratorOptions.find(option => option.value === modModerator);
                                        if (found) {
                                            return found;
                                        } else {
                                            return null;
                                        }
                                    })()
                                }
                                onChange={(option) => {
                                    if (option) {
                                        setModModerator(option.value);
                                    } else {
                                        setModModerator("");
                                    }
                                }}
                                className="module-input"
                                classNamePrefix="react-select"
                                placeholder="Select Moderator"
                            />

                            <label className="label">Module Staff</label>
                            <Select
                                isMulti
                                options={filteredStaffOptions}
                                value={filteredStaffOptions.filter(option => modStaff.includes(option.value))}
                                onChange={(selected) => {
                                    setModStaff(selected.map(option => option.value));
                                }}
                                className="module-input"
                                classNamePrefix="react-select"
                                placeholder="Select module staff"
                            />

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