import React, { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import API from "../../shared/api/api";
import "./admin.css";

const STATUS_OPTIONS = ["PENDING", "COMPLETED", "CANCELLED"];

function AdminDashboard() {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("products");

  const [foods, setFoods] = useState([]);
  const [orders, setOrders] = useState([]);
  const [updatingOrderId, setUpdatingOrderId] = useState(null);
  const [statusMsg, setStatusMsg] = useState("");

  // For adding / editing food
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [foodForm, setFoodForm] = useState({
    name: "",
    description: "",
    price: "",
    category: "",
    img: "",
  });

  const fetchFoods = useCallback(async () => {
    try {
      const res = await API.get("/foods");
      if (res.data.success) setFoods(res.data.data);
    } catch (err) {
      console.error(err);
    }
  }, []);

  const fetchOrders = useCallback(async () => {
    try {
      const res = await API.get("/orders");
      if (res.data.success) setOrders(res.data.data);
    } catch (err) {
      console.error(err);
    }
  }, []);

  useEffect(() => {
    fetchFoods();
    fetchOrders();
  }, [fetchFoods, fetchOrders]);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  // ── Food handlers ─────────────────────────────────────────────────
  const handleFoodFormChange = (e) => {
    setFoodForm({ ...foodForm, [e.target.name]: e.target.value });
  };

  const submitFood = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await API.put(`/foods/${editingId}`, foodForm);
      } else {
        await API.post("/foods", foodForm);
      }
      setShowForm(false);
      setFoodForm({ name: "", description: "", price: "", category: "", img: "" });
      setEditingId(null);
      fetchFoods();
    } catch (err) {
      alert("Failed to save food item.");
    }
  };

  const deleteFood = async (id) => {
    if (!window.confirm("Delete this food item?")) return;
    try {
      await API.delete(`/foods/${id}`);
      fetchFoods();
    } catch (err) {
      alert("Failed to delete food item.");
    }
  };

  const editFood = (food) => {
    setFoodForm({
      name: food.name,
      description: food.description,
      price: food.price,
      category: food.category,
      img: food.img,
    });
    setEditingId(food.id);
    setShowForm(true);
  };

  // ── Order status update ────────────────────────────────────────────
  const handleStatusChange = async (orderId, newStatus) => {
    setUpdatingOrderId(orderId);
    try {
      const res = await API.patch(`/orders/${orderId}/status`, { status: newStatus });
      if (res.data.success) {
        setOrders((prev) =>
          prev.map((o) => (o.id === orderId ? { ...o, status: res.data.data.status } : o))
        );
        setStatusMsg(`Order #${orderId} → ${newStatus}`);
        setTimeout(() => setStatusMsg(""), 2500);
      }
    } catch (err) {
      alert("Failed to update order status.");
    } finally {
      setUpdatingOrderId(null);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case "COMPLETED": return "#27ae60";
      case "CANCELLED": return "#e74c3c";
      default: return "#f39c12";
    }
  };

  return (
    <div className="admin-wrapper">

      {/* ── HEADER ── */}
      <div className="admin-header">
        <div className="admin-logo">
          <span>🍔</span>
          <span>Foodie Admin</span>
        </div>
        <button className="admin-logout-btn" onClick={handleLogout}>
          Logout
        </button>
      </div>

      {/* ── TABS ── */}
      <div className="admin-tabs">
        <button
          className={`admin-tab${activeTab === "products" ? " admin-tab-active" : ""}`}
          onClick={() => setActiveTab("products")}
        >
          🥗 Manage Products
        </button>
        <button
          className={`admin-tab${activeTab === "orders" ? " admin-tab-active" : ""}`}
          onClick={() => { setActiveTab("orders"); fetchOrders(); }}
        >
          📦 All Orders
        </button>
      </div>

      {/* ── TOAST ── */}
      {statusMsg && (
        <div className="admin-toast">✅ {statusMsg}</div>
      )}

      <div className="admin-content">

        {/* ════════════ PRODUCTS TAB ════════════ */}
        {activeTab === "products" && (
          <div>
            <button
              className="admin-add-btn"
              onClick={() => {
                setShowForm(!showForm);
                setEditingId(null);
                setFoodForm({ name: "", description: "", price: "", category: "", img: "" });
              }}
            >
              {showForm ? "✕ Close Form" : "+ Add New Food"}
            </button>

            {showForm && (
              <form className="admin-form" onSubmit={submitFood}>
                <h3>{editingId ? "Edit Food Item" : "Add New Food Item"}</h3>
                <div className="admin-form-grid">
                  <input
                    className="admin-input"
                    name="name"
                    value={foodForm.name}
                    onChange={handleFoodFormChange}
                    placeholder="Food Name"
                    required
                  />
                  <input
                    className="admin-input"
                    name="category"
                    value={foodForm.category}
                    onChange={handleFoodFormChange}
                    placeholder="Category (e.g. Burger)"
                    required
                  />
                  <input
                    className="admin-input"
                    type="number"
                    name="price"
                    value={foodForm.price}
                    onChange={handleFoodFormChange}
                    placeholder="Price (₱)"
                    required
                  />
                  <input
                    className="admin-input"
                    name="img"
                    value={foodForm.img}
                    onChange={handleFoodFormChange}
                    placeholder="Image URL"
                    required
                  />
                </div>
                <input
                  className="admin-input"
                  name="description"
                  value={foodForm.description}
                  onChange={handleFoodFormChange}
                  placeholder="Description"
                  style={{ width: "100%", marginTop: "10px" }}
                  required
                />
                <div style={{ marginTop: "16px" }}>
                  <button type="submit" className="admin-save-btn">
                    💾 Save
                  </button>
                </div>
              </form>
            )}

            <table className="admin-table">
              <thead>
                <tr>
                  <th>Image</th>
                  <th>Name</th>
                  <th>Price</th>
                  <th>Category</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {foods.length === 0 && (
                  <tr>
                    <td colSpan={5} style={{ textAlign: "center", color: "#aaa", padding: "30px" }}>
                      No food items yet. Add one above!
                    </td>
                  </tr>
                )}
                {foods.map((f) => (
                  <tr key={f.id}>
                    <td>
                      <img
                        src={f.img}
                        alt={f.name}
                        className="admin-food-img"
                        onError={(e) => { e.target.src = "https://via.placeholder.com/50"; }}
                      />
                    </td>
                    <td>
                      <div style={{ fontWeight: 600 }}>{f.name}</div>
                      {f.description && (
                        <div style={{ fontSize: "12px", color: "#888", marginTop: "2px" }}>
                          {f.description}
                        </div>
                      )}
                    </td>
                    <td style={{ fontWeight: 600 }}>₱{f.price}</td>
                    <td>
                      <span className="admin-category-badge">{f.category}</span>
                    </td>
                    <td>
                      <button className="admin-edit-btn" onClick={() => editFood(f)}>
                        ✏️ Edit
                      </button>
                      <button className="admin-delete-btn" onClick={() => deleteFood(f.id)}>
                        🗑️ Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* ════════════ ORDERS TAB ════════════ */}
        {activeTab === "orders" && (
          <div>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px" }}>
              <h3 style={{ margin: 0, fontSize: "20px", fontWeight: 700 }}>
                All Customer Orders
              </h3>
              <button className="admin-refresh-btn" onClick={fetchOrders}>
                🔄 Refresh
              </button>
            </div>

            {orders.length === 0 ? (
              <div style={{ textAlign: "center", padding: "60px 0", color: "#aaa" }}>
                <div style={{ fontSize: "48px", marginBottom: "12px" }}>📭</div>
                <p>No orders yet.</p>
              </div>
            ) : (
              <table className="admin-table">
                <thead>
                  <tr>
                    <th>Order ID</th>
                    <th>Customer</th>
                    <th>Items</th>
                    <th>Total</th>
                    <th>Status</th>
                    <th>Update Status</th>
                  </tr>
                </thead>
                <tbody>
                  {orders.map((o) => (
                    <tr key={o.id}>
                      <td style={{ fontWeight: 700 }}>#{o.id}</td>
                      <td>{o.customerName}</td>
                      <td>
                        <div style={{ maxHeight: "80px", overflowY: "auto" }}>
                          {o.items && o.items.map((item) => (
                            <div key={item.id} style={{ fontSize: "12px", color: "#555", padding: "2px 0" }}>
                              • {item.quantity}x {item.food?.name || "Item"}
                            </div>
                          ))}
                        </div>
                      </td>
                      <td style={{ fontWeight: 700 }}>₱{o.totalAmount}</td>
                      <td>
                        <span style={{
                          display: "inline-block",
                          padding: "4px 12px",
                          borderRadius: "20px",
                          fontSize: "12px",
                          fontWeight: 700,
                          color: getStatusColor(o.status),
                          background: o.status === "COMPLETED" ? "#d4edda"
                            : o.status === "CANCELLED" ? "#fde8e8" : "#fff3cd",
                        }}>
                          {o.status}
                        </span>
                      </td>
                      <td>
                        <select
                          className="admin-status-select"
                          value={o.status}
                          onChange={(e) => handleStatusChange(o.id, e.target.value)}
                          disabled={updatingOrderId === o.id}
                          style={{
                            borderColor: getStatusColor(o.status),
                            color: getStatusColor(o.status),
                          }}
                        >
                          {STATUS_OPTIONS.map((s) => (
                            <option key={s} value={s}>{s}</option>
                          ))}
                        </select>
                        {updatingOrderId === o.id && (
                          <span style={{ marginLeft: "8px", fontSize: "12px", color: "#888" }}>
                            Saving...
                          </span>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default AdminDashboard;
