import React from "react";
import Navbar from "../components/Navbar.jsx";
import Footer from "../components/Footer.jsx";

export default function UserCreation(){
    return (
        <>
            <Navbar left="COM2008 Systems Design and Security" right="Exam officer"></Navbar>
            <p>Hello World</p>
            <div className = "footer-box">
                <Footer/>
            </div>
        </>
    );
}