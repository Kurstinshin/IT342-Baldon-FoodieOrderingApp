import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../services/api";
import "../styles/auth.css";

function Register() {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleRegister = async (e) => {
  e.preventDefault();

  try {
    const res = await API.post("/auth/register", {
      name,
      email,
      password,
    });

    alert(res.data.message || "Registered successfully");
    navigate("/");

  } catch (err) {
    alert(err.response?.data || "Error registering");
  }
};

  return (
    <div className="auth-page">

      <div className="auth-card">

        <img
          className="auth-image"
          src="https://images.unsplash.com/photo-1540189549336-e6e99c3679fe"
          alt="food"
        />

        <div className="auth-content">

          <div className="app-name">
            FoodieOrderingApp
          </div>

          <div className="tabs">
            <div className="tab" onClick={() => navigate("/")}>Sign In</div>
            <div className="tab active">Sign Up</div>
          </div>

          <form onSubmit={handleRegister}>

            <input className="input"
              placeholder="Full Name"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />

            <input className="input"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />

            <input className="input"
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            <button className="btn" type="submit">
              Create Account
            </button>

          </form>

        </div>
      </div>

    </div>
  );
}

export default Register;