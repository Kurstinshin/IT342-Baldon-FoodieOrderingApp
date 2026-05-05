import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../services/api";
import "../styles/auth.css";

function Login() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const res = await API.post("/auth/login", { email, password });

      const data = res.data.data;
      localStorage.setItem("token", data.accessToken);
      localStorage.setItem("userId", data.user.id);
      localStorage.setItem("userName", `${data.user.firstname} ${data.user.lastname}`);
      localStorage.setItem("userEmail", data.user.email);
      localStorage.setItem("userRole", data.user.role);

      if (data.user.role === "ADMIN") {
        navigate("/admin");
      } else {
        navigate("/dashboard");
      }

    } catch (err) {
      const msg = err.response?.data?.error?.message || "Login failed. Please try again.";
      alert(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">

        {/* IMAGE */}
        <img
          className="auth-image"
          src="https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=600&q=80"
          alt="food"
        />

        <div className="auth-content">

          <div className="app-name">🍽️ FoodieOrderingApp</div>

          {/* TABS */}
          <div className="tabs">
            <div className="tab active">Sign In</div>
            <div className="tab" onClick={() => navigate("/register")}>Sign Up</div>
          </div>

          {/* FORM */}
          <form onSubmit={handleLogin}>

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

            <div className="forgot">Forgot password?</div>

            <button className="btn" type="submit" disabled={loading}>
              {loading ? "Logging in..." : "Login"}
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

export default Login;