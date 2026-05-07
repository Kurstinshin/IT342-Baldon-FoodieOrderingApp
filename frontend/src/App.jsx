/**
 * App.jsx — Routing Root
 *
 * Vertical Slice Architecture: imports are grouped by feature.
 * Each feature owns its page component alongside its styles and context.
 */
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { CartProvider } from "./features/cart/CartContext";

// ── Feature Pages ──────────────────────────────────────────────────────────
import Home              from "./features/home/HomePage";
import LoginPage         from "./features/auth/LoginPage";
import RegisterPage      from "./features/auth/RegisterPage";
import DashboardPage     from "./features/dashboard/DashboardPage";
import CartPage          from "./features/cart/CartPage";
import CheckoutPage      from "./features/checkout/CheckoutPage";
import OrderHistoryPage  from "./features/orders/OrderHistoryPage";
import AdminDashboardPage from "./features/admin/AdminDashboardPage";

// Static pages
import About   from "./features/static/About";
import Service from "./features/static/Service";
import Blog    from "./features/static/Blog";
import Contact from "./features/static/Contact";

// ── Guards ─────────────────────────────────────────────────────────────────
function PrivateRoute({ children }) {
  const token = localStorage.getItem("token");
  return token ? children : <Navigate to="/login" replace />;
}

function AdminRoute({ children }) {
  const role = localStorage.getItem("userRole");
  return role === "ADMIN" ? children : <Navigate to="/dashboard" replace />;
}

// ── App ────────────────────────────────────────────────────────────────────
function App() {
  return (
    <CartProvider>
      <BrowserRouter>
        <Routes>
          {/* 🏠 Home feature */}
          <Route path="/" element={<Home />} />

          {/* 🔐 Auth feature */}
          <Route path="/login"    element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* 🍔 Dashboard feature */}
          <Route path="/dashboard" element={<PrivateRoute><DashboardPage /></PrivateRoute>} />

          {/* 🛒 Cart feature */}
          <Route path="/cart" element={<PrivateRoute><CartPage /></PrivateRoute>} />

          {/* 💳 Checkout feature */}
          <Route path="/checkout" element={<PrivateRoute><CheckoutPage /></PrivateRoute>} />

          {/* 📦 Orders feature */}
          <Route path="/orders" element={<PrivateRoute><OrderHistoryPage /></PrivateRoute>} />

          {/* 👑 Admin feature */}
          <Route path="/admin" element={<AdminRoute><AdminDashboardPage /></AdminRoute>} />

          {/* 📄 Static feature */}
          <Route path="/about"   element={<About />} />
          <Route path="/service" element={<Service />} />
          <Route path="/blog"    element={<Blog />} />
          <Route path="/contact" element={<Contact />} />
        </Routes>
      </BrowserRouter>
    </CartProvider>
  );
}

export default App;