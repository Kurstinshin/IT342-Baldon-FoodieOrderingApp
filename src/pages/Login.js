import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import API from "../services/api";
import "../styles/auth.css";

function Login() {

  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    try {

      const res = await API.post("/login", {
        email,
        password
      });

      alert("Login successful");

      navigate("/home");

    } catch (err) {
      alert("Invalid email or password");
    }
  };

  return (
    <div className="auth-container">

      <div className="auth-box">

        <h2>FoodieOrdering</h2>
        <h3>Login</h3>

        <form onSubmit={handleLogin}>

          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e)=>setEmail(e.target.value)}
            required
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e)=>setPassword(e.target.value)}
            required
          />

          <button type="submit">Login</button>

        </form>

        <p>
          Don't have an account?
          <Link to="/register"> Register</Link>
        </p>

      </div>

    </div>
  );
}

export default Login;