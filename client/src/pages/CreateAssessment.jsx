import { useState, useEffect } from "react";
import axios from "axios";
import "./CreateAssessment.css"
import Navbar from "../components/Navbar.jsx";
import { Dropdown } from "bootstrap";


export default function CreateAssessment() {
    const [name, setName] = useState("");
    const [type, setType] = useState(null);
    const [description, setDescription] = useState("");

    const [csvFile, setCsvFile] = useState(null);
    const [attachment, setAttachment] = useState(null);

    // fetch modules from api
    const [modules, setModules] = useState([]);
    // TODO: only fetch modules that have the user/all for admin
    useEffect(() => {
        fetch("http://localhost:8080/api/modules")
            .then(res => res.json())
            .then(data => setModules(data));
    })

    // this is for later, get list of staff to set as setter/checker
    // const [moduleStaff, setModuleStaff] = useState([]);
    // useEffect(() => {
    //     fetch("http://localhost:8080/" + currentModuleId + "/modules")
    //         .then(res => res.json())
    //         .then(data => setModules(data));
    // })
    //
    const [moduleId, setModuleId] = useState(0);

    const onModuleSelect = (event) => {
        setModuleId(event.target.value);
    };

    const Create = async () => {
        try {
            if (!name || !type) {
                alert("Please enter name and select a type.");
                return;
            }

            const payload = {
                name: name,
                type: type.toUpperCase(),
                moduleId: moduleId,
                // setterId 
                // checkerId
                description: description
            };

            console.log("payload send:", payload);

            const response = await axios.post("http://localhost:8080/api/assessments", payload);

            console.log("Created:", response.data);
            alert("Assessment created!");
        } catch (err) {
            console.error(err);
            alert("Failed to create assessment.");
        }
    };

    const CsvUpload = async () => {
        const formData = new FormData();
        formData.append("file", csvFile);

        await axios.post("http://localhost:8080/api/assessments/uploadCsv", formData, {
            headers: { "Content-Type": "multipart/form-data" }
        });

        alert("CSV uploaded!");
    };

    const AttachmentUpload = async () => {
        const formData = new FormData();
        formData.append("file", attachment);
        await axios.post("http://localhost:8080/api/assessments/uploadAttachment", formData, {
            headers: { "Content-Type": "multipart/form-data" }
        })
        alert("Attachment uploaded!");
    }

    return (
        <>
            <Navbar left="COM2008 Systems Design and Security" right="Exam officer" />
            <div className="assessment-page">
                <div className="assessment-container">

                    <h1 className="title">Create New Assessment</h1>

                    <div className="grid-container">

                        {/* left area */}
                        <div className="left-column">

                            <label className="label">Assessment Name</label>
                            <input
                                className="input"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                            />

                            <label className="label">For Module</label>
                            <select value={moduleId} onChange={onModuleSelect}>
                                <option value="">-- Select Module --</option>
                                {modules.map((m) => (
                                    <option key={m.id} value={m.id}>
                                        {m.moduleName}
                                    </option>
                                ))}
                            </select>

                            <label className="label">Assessment Type</label>
                            <div className="type-buttons">
                                <button
                                    className={`type-btn ${type === "COURSEWORK" ? "selected" : ""}`}
                                    onClick={() => setType("COURSEWORK")}
                                    onDoubleClick={() => setType(null)}
                                >
                                    <span>Coursework</span>
                                    <span className={`circle ${type === "COURSEWORK" ? "checked" : ""}`}>
                                        {type === "COURSEWORK" && <span className="tick">✔</span>}
                                    </span>
                                </button>
                                <button
                                    className={`type-btn ${type === "test" ? "selected" : ""}`}
                                    onClick={() => setType("test")}
                                    onDoubleClick={() => setType(null)}
                                >
                                    <span>In-semester quiz</span>
                                    <span className={`circle ${type === "test" ? "checked" : ""}`}>
                                        {type === "test" && <span className="tick">✔</span>}
                                    </span>
                                </button>
                                <button
                                    className={`type-btn ${type === "exam" ? "selected" : ""}`}
                                    onClick={() => setType("exam")}
                                    onDoubleClick={() => setType(null)}
                                >
                                    <span>Exam</span>
                                    <span className={`circle ${type === "exam" ? "checked" : ""}`}>
                                        {type === "exam" && <span className="tick">✔</span>}
                                    </span>
                                </button>
                            </div>

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
                                    <img src="/icons/file.svg" alt="file upload icon" className="file-icon" />
                                </div>

                                <button className="attach-btn" onClick={CsvUpload}>
                                    Attach
                                </button>
                            </div>
                        </div>

                        {/* right area */}
                        <div className="right-column">
                            <label className="label">Attachments</label>
                            <div className="attach-wrapper">
                                <input
                                    className="attach-input"
                                    type="file"
                                    onChange={(e) => setAttachment(e.target.files[0])}
                                />

                                <div className="file-display">
                                    <img src="/icons/file.svg" alt="file upload icon" className="file-icon" />
                                </div>

                                <button className="attach-btn" onClick={AttachmentUpload}>Attach</button>
                            </div>
                            <label className="label">Description</label>
                            <textarea
                                className="textarea"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                            ></textarea>

                            <button className="create-btn" onClick={Create}>
                                Create
                            </button>
                        </div>

                    </div>

                </div>

            </div>
        </>
    );
}
