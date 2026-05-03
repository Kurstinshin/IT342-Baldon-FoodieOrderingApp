import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Cart from "./pages/Cart";
import Checkout from "./pages/Checkout";
import AdminDashboard from "./pages/AdminDashboard";
import OrderHistory from "./pages/OrderHistory";

import About from "./pages/About";
import Service from "./pages/Service";
import Blog from "./pages/Blog";
import Contact from "./pages/Contact";

function PrivateRoute({ children }) {
  const token = localStorage.getItem("token");
  return token ? children : <Navigate to="/login" replace />;
}

function AdminRoute({ children }) {
  const role = localStorage.getItem("userRole");
  return role === "ADMIN" ? children : <Navigate to="/dashboard" replace />;
}

function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* 🏠 LANDING */}
        <Route path="/" element={<Home />} />

        {/* 🔐 AUTH */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* 🍔 MAIN - PROTECTED */}
        <Route path="/dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} />

        {/* 🛒 CART - PROTECTED */}
        <Route path="/cart" element={<PrivateRoute><Cart /></PrivateRoute>} />

        {/* 💳 CHECKOUT - PROTECTED */}
        <Route path="/checkout" element={<PrivateRoute><Checkout /></PrivateRoute>} />

        {/* 📦 ORDERS - PROTECTED */}
        <Route path="/orders" element={<PrivateRoute><OrderHistory /></PrivateRoute>} />

        {/* 👑 ADMIN - PROTECTED */}
        <Route path="/admin" element={<AdminRoute><AdminDashboard /></AdminRoute>} />

        {/* 📄 PAGES */}
        <Route path="/about" element={<About />} />
        <Route path="/service" element={<Service />} />
        <Route path="/blog" element={<Blog />} />
        <Route path="/contact" element={<Contact />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;