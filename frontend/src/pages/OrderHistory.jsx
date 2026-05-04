import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import API from "../services/api";
import "../styles/order-history.css";

function OrderHistory() {
  const navigate = useNavigate();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchOrders = async () => {
    const userId = localStorage.getItem("userId");
    if (!userId) {
      setLoading(false);
      return;
    }

    try {
      const res = await API.get(`/orders/${userId}`);
      if (res.data.success) {
        setOrders(res.data.data);
      }
    } catch (err) {
      console.error("Failed to fetch orders", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const getStatusColor = (status) => {
    switch (status) {
      case "COMPLETED": return "#27ae60";
      case "CANCELLED": return "#e74c3c";
      default: return "#f39c12";
    }
  };

  const getStatusBg = (status) => {
    switch (status) {
      case "COMPLETED": return "#d4edda";
      case "CANCELLED": return "#fde8e8";
      default: return "#fff3cd";
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case "COMPLETED": return "✅";
      case "CANCELLED": return "❌";
      default: return "⏳";
    }
  };

  return (
    <div className="oh-page">

      {/* ── TOP BAR ── */}
      <div className="oh-topbar">
        <button className="oh-back-btn" onClick={() => navigate("/dashboard")}>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none"
            stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round">
            <path d="M15 18l-6-6 6-6" />
          </svg>
        </button>
        <h2 className="oh-title">Order History</h2>
        <button className="oh-refresh-btn" onClick={fetchOrders} title="Refresh">
          🔄
        </button>
      </div>

      <div className="oh-body">
        {loading ? (
          <div className="oh-loading">
            <div className="oh-spinner" />
            <p>Loading your orders...</p>
          </div>
        ) : orders.length === 0 ? (
          <div className="oh-empty">
            <div className="oh-empty-icon">🍽️</div>
            <h3>No orders yet!</h3>
            <p>Hungry? Start ordering some delicious food.</p>
            <button className="oh-browse-btn" onClick={() => navigate("/dashboard")}>
              Browse Food
            </button>
          </div>
        ) : (
          <div className="oh-list">
            {orders.map((order) => (
              <div key={order.id} className="oh-card">
                {/* Card Header */}
                <div className="oh-card-header">
                  <div>
                    <span className="oh-order-id">Order #{order.id}</span>
                    <span className="oh-customer">👤 {order.customerName}</span>
                  </div>
                  <span
                    className="oh-status-badge"
                    style={{
                      background: getStatusBg(order.status),
                      color: getStatusColor(order.status),
                    }}
                  >
                    {getStatusIcon(order.status)} {order.status}
                  </span>
                </div>

                {/* Items */}
                <div className="oh-items">
                  {order.items && order.items.map((item) => (
                    <div key={item.id} className="oh-item-row">
                      <span>{item.quantity}x {item.food?.name || "Item"}</span>
                      <span>₱{item.food ? (item.food.price * item.quantity).toFixed(2) : "0.00"}</span>
                    </div>
                  ))}
                </div>

                {/* Footer */}
                <div className="oh-card-footer">
                  <span className="oh-total">Total: ₱{Number(order.totalAmount).toFixed(2)}</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default OrderHistory;
