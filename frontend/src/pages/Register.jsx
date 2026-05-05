import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../services/api";
import "../styles/auth.css";

function Register() {
  const navigate = useNavigate();

  const [firstname, setFirstname] = useState("");
  const [lastname, setLastname] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleRegister = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const res = await API.post("/auth/register", { firstname, lastname, email, password });
      alert(res.data.message || "User registered successfully");
      navigate("/login");
    } catch (err) {
      const msg = err.response?.data?.error?.message || "Error registering. Please try again.";
      alert(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">

        <img
          className="auth-image"
          src="https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=600&q=80"
          alt="food"
        />

        <div className="auth-content">

          <div className="app-name">🍽️ FoodieOrderingApp</div>

          {/* TABS */}
          <div className="tabs">
            <div className="tab" onClick={() => navigate("/login")}>Sign In</div>
            <div className="tab active">Sign Up</div>
          </div>

          {/* FORM */}
          <form onSubmit={handleRegister}>

            <input
              className="input"
              type="text"
              placeholder="First Name"
              value={firstname}
              onChange={(e) => setFirstname(e.target.value)}
              required
            />

            <input
              className="input"
              type="text"
              placeholder="Last Name"
              value={lastname}
              onChange={(e) => setLastname(e.target.value)}
              required
            />

            <input
              className="input"
              type="email"
              placeholder="Email address"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />

            <input
              className="input"
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

            <button className="btn" type="submit" disabled={loading}>
              {loading ? "Creating account..." : "Create Account"}
            </button>

          </form>

          <div className="or">OR</div>

          <div className="social">
            <img src="https://cdn-icons-png.flaticon.com/512/2991/2991148.png" alt="google" />
            <img src="https://cdn-icons-png.flaticon.com/512/124/124010.png" alt="facebook" />
            <img src="https://cdn-icons-png.flaticon.com/512/733/733579.png" alt="twitter" />
          </div>

        </div>
      </div>
    </div>
  );
}

export default Register;