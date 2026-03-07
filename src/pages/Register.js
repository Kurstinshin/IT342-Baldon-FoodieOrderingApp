import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import API from "../services/api";
import "../styles/auth.css";

function Register() {

  const navigate = useNavigate();

  const [name,setName] = useState("");
  const [email,setEmail] = useState("");
  const [password,setPassword] = useState("");

  const handleRegister = async (e) => {

    e.preventDefault();

    try {

      await API.post("/register",{
        name,
        email,
        password
      });

      alert("Account created successfully");

      navigate("/");

    } catch(err){
      alert("Email already exists");
    }
  };

  return (
    <div className="auth-container">

      <div className="auth-box">

        <h2>FoodieOrderingApp</h2>
        <h3>Create Account</h3>

        <form onSubmit={handleRegister}>

          <input
            type="text"
            placeholder="Full Name"
            value={name}
            onChange={(e)=>setName(e.target.value)}
            required
          />

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

          <button type="submit">Register</button>

        </form>

        <p>
          Already have an account?
          <Link to="/"> Login</Link>
        </p>

      </div>

    </div>
  );
}

export default Register;