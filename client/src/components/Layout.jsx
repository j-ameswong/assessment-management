import Navbar from "./Navbar";
import "./Layout.css";

export default function Layout({ children }) {
  return (
    <>
      <Navbar />

      <main className="layout-content">
        {children}
      </main>
    </>
  );
}
