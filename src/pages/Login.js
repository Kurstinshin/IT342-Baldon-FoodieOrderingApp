
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../services/api";
import "../styles/auth.css";

function Login() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
  e.preventDefault();

  try {
    const res = await API.post("/auth/login", { email, password });

    console.log("LOGIN RESPONSE:", res.data);

    
    if (res.data.token) {
      localStorage.setItem("token", res.data.token);
      navigate("/dashboard");
    } else {
      alert(res.data.message || "Login failed");
    }

  } catch (err) {
    console.log(err);
    alert(err.response?.data || "Server error");
  }
};

  return (
    <div className="auth-page">

      <div className="auth-card">

        {/* IMAGE */}
        <img
          className="auth-image"
          src="https://images.unsplash.com/photo-1504674900247-0877df9cc836"
          alt="food"
        />

        <div className="auth-content">

          <div className="app-name">
            FoodieOrderingApp
          </div>

          {/* TABS */}
          <div className="tabs">
            <div className="tab active">Sign In</div>
            <div className="tab" onClick={() => navigate("/register")}>
              Sign Up
            </div>
          </div>

          {/* FORM */}
          <form onSubmit={handleLogin}>
            <input
              className="input"
              type="email"
              placeholder="E-mail address"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />

            <input
              className="input"
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            <div className="forgot">Forgot password?</div>

            <button className="btn" type="submit">
              Login
            </button>
          </form>

          <div className="or">OR</div>

          <div className="social">
            <img src="https://cdn-icons-png.flaticon.com/512/2991/2991148.png" />
            <img src="https://cdn-icons-png.flaticon.com/512/124/124010.png" />
            <img src="https://cdn-icons-png.flaticon.com/512/733/733579.png" />
          </div>

        </div>
      </div>
    </div>
  );
}

export default Login;