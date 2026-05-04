import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "../cart/CartContext";
import API from "../../shared/api/api";
import "./checkout.css";

function Checkout() {
  const navigate = useNavigate();
  const { cart, fetchCart } = useCart();

  const [form, setForm] = useState({
    name: "",
    address: "",
    payment: "Card",
    cardNumber: "",
  });

  const [placed, setPlaced] = useState(false);
  const [loading, setLoading] = useState(false);
  const [placedOrder, setPlacedOrder] = useState(null);
  const [orderHistory, setOrderHistory] = useState([]);
  const [historyLoading, setHistoryLoading] = useState(false);

  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const fetchOrderHistory = async (userId) => {
    setHistoryLoading(true);
    try {
      const res = await API.get(`/orders/${userId}`);
      if (res.data.success) {
        setOrderHistory(res.data.data);
      }
    } catch (err) {
      console.error("Failed to fetch order history:", err);
    } finally {
      setHistoryLoading(false);
    }
  };

  const handleOrder = async (e) => {
    e.preventDefault();
    if (cart.length === 0) {
      alert("Your cart is empty!");
      return;
    }

    const userId = localStorage.getItem("userId");
    if (!userId) {
      alert("Please log in to place an order");
      return;
    }

    setLoading(true);
    try {
      const res = await API.post(`/orders/${userId}`, {
        customerName: form.name,
      });

      if (res.data.success) {
        setPlacedOrder(res.data.data);
        await fetchCart();
        await fetchOrderHistory(userId);
        setPlaced(true);
      }
    } catch (err) {
      console.error("Checkout failed:", err);
      alert("Checkout failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case "COMPLETED": return "#27ae60";
      case "CANCELLED": return "#e74c3c";
      default: return "#f39c12"; // PENDING
    }
  };

  const getStatusBg = (status) => {
    switch (status) {
      case "COMPLETED": return "#d4edda";
      case "CANCELLED": return "#fde8e8";
      default: return "#fff3cd"; // PENDING
    }
  };

  if (placed) {
    return (
      <div className="checkout-page">
        {/* ── SUCCESS BANNER ── */}
        <div className="order-success">
          <div className="success-icon">🎉</div>
          <h2>Order Placed!</h2>
          <p>
            Thank you, <strong>{form.name}</strong>! Your food is being prepared.
          </p>
          <p>
            Delivering to: <strong>{form.address}</strong>
          </p>

          {placedOrder && (
            <div style={{
              background: "#f9f9f9",
              border: "1px solid #eee",
              borderRadius: "12px",
              padding: "16px 20px",
              marginTop: "20px",
              maxWidth: "420px",
              width: "100%",
              textAlign: "left"
            }}>
              <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "10px" }}>
                <strong style={{ fontSize: "14px" }}>Order #{placedOrder.id}</strong>
                <span style={{
                  background: getStatusBg(placedOrder.status),
                  color: getStatusColor(placedOrder.status),
                  padding: "3px 10px",
                  borderRadius: "20px",
                  fontSize: "12px",
                  fontWeight: 700
                }}>
                  {placedOrder.status}
                </span>
              </div>
              {placedOrder.items && placedOrder.items.map((item) => (
                <div key={item.id} style={{
                  display: "flex",
                  justifyContent: "space-between",
                  fontSize: "13px",
                  padding: "5px 0",
                  borderBottom: "1px dashed #ddd"
                }}>
                  <span>{item.quantity}x {item.food?.name || "Item"}</span>
                  <span>₱{item.food ? item.food.price * item.quantity : 0}</span>
                </div>
              ))}
              <div style={{
                display: "flex",
                justifyContent: "space-between",
                fontWeight: 700,
                marginTop: "10px",
                fontSize: "14px"
              }}>
                <span>Total</span>
                <span>₱{placedOrder.totalAmount}</span>
              </div>
            </div>
          )}
        </div>

        {/* ── FULL ORDER HISTORY ── */}
        <div style={{
          maxWidth: "600px",
          margin: "0 auto",
          padding: "0 24px 40px"
        }}>
          <h3 style={{
            fontSize: "18px",
            fontWeight: 700,
            marginBottom: "16px",
            color: "#222"
          }}>
            📦 Your Order History
          </h3>

          {historyLoading ? (
            <p style={{ color: "#aaa", textAlign: "center" }}>Loading history...</p>
          ) : orderHistory.length === 0 ? (
            <p style={{ color: "#aaa", textAlign: "center" }}>No orders yet.</p>
          ) : (
            orderHistory.map((order) => (
              <div key={order.id} style={{
                border: "1px solid #eee",
                borderRadius: "12px",
                padding: "16px",
                marginBottom: "14px",
                background: "#fff",
                boxShadow: "0 2px 8px rgba(0,0,0,0.05)"
              }}>
                <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "10px" }}>
                  <strong style={{ fontSize: "14px", color: "#222" }}>Order #{order.id}</strong>
                  <span style={{
                    background: getStatusBg(order.status),
                    color: getStatusColor(order.status),
                    padding: "3px 10px",
                    borderRadius: "20px",
                    fontSize: "12px",
                    fontWeight: 700
                  }}>
                    {order.status}
                  </span>
                </div>

                <p style={{ margin: "4px 0 8px", fontSize: "13px", color: "#777" }}>
                  Customer: {order.customerName}
                </p>

                <div style={{ background: "#f9f9f9", borderRadius: "8px", padding: "10px" }}>
                  {order.items && order.items.map((item) => (
                    <div key={item.id} style={{
                      display: "flex",
                      justifyContent: "space-between",
                      fontSize: "13px",
                      padding: "4px 0",
                      borderBottom: "1px dashed #ddd"
                    }}>
                      <span>{item.quantity}x {item.food?.name || "Item"}</span>
                      <span>₱{item.food ? item.food.price * item.quantity : 0}</span>
                    </div>
                  ))}
                </div>

                <div style={{
                  display: "flex",
                  justifyContent: "flex-end",
                  marginTop: "10px",
                  fontWeight: 700,
                  fontSize: "15px",
                  color: "#222"
                }}>
                  Total: ₱{order.totalAmount}
                </div>
              </div>
            ))
          )}

          <div style={{ display: "flex", gap: "12px", marginTop: "24px" }}>
            <button
              className="done-btn"
              onClick={() => navigate("/dashboard")}
              style={{ flex: 1 }}
            >
              Order More Food
            </button>
            <button
              className="done-btn"
              onClick={() => navigate("/orders")}
              style={{
                flex: 1,
                background: "linear-gradient(135deg, #6a1b9a, #9c27b0)"
              }}
            >
              View All Orders
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="checkout-page">
      {/* ── HEADER ── */}
      <div className="checkout-header">
        <span className="back-icon" onClick={() => navigate("/cart")}>
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none"
            stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round">
            <path d="M15 18l-6-6 6-6" />
          </svg>
        </span>
        <h2>Payment</h2>
        <span style={{ width: 24 }} />
      </div>

      {/* ── PROGRESS BAR ── */}
      <div className="progress-bar-container">
        <div className="progress-line" />
        <div className="progress-steps">
          {[0, 1, 2].map((i) => (
            <div key={i} className="step-circle active">
              <svg width="12" height="12" viewBox="0 0 24 24" fill="none"
                stroke="currentColor" strokeWidth="4" strokeLinecap="round" strokeLinejoin="round">
                <polyline points="20 6 9 17 4 12" />
              </svg>
            </div>
          ))}
        </div>
      </div>

      {/* ── PAYMENT FORM ── */}
      <form className="payment-form" onSubmit={handleOrder}>

        <div className="form-group">
          <label>Delivery Address</label>
          <div className="input-wrapper">
            <span className="input-icon">📍</span>
            <input
              type="text"
              name="address"
              value={form.address}
              onChange={handleChange}
              className="checkout-input"
              placeholder="Enter delivery address"
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label>Cardholder Name</label>
          <div className="input-wrapper">
            <span className="input-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="#fbd65b"
                stroke="#6a1b9a" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>
            </span>
            <input
              type="text"
              name="name"
              value={form.name}
              onChange={handleChange}
              className="checkout-input"
              placeholder="Full name"
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label>Card Number</label>
          <div className="input-wrapper">
            <span className="input-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="#fbd65b"
                stroke="#6a1b9a" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <rect x="1" y="4" width="22" height="16" rx="2" ry="2" />
                <line x1="1" y1="10" x2="23" y2="10" />
              </svg>
            </span>
            <input
              type="text"
              name="cardNumber"
              value={form.cardNumber}
              onChange={handleChange}
              className="checkout-input"
              placeholder="XXXX XXXX XXXX XXXX"
              required
            />
          </div>
        </div>

        <div className="divider" />

        <div className="amount-payable">
          <span>Amount Payable</span>
          <span className="amount-total">₱{total.toFixed(2)}</span>
        </div>

        <button className="place-order-btn" type="submit" disabled={loading}>
          {loading ? "Processing..." : "Pay Now"}
        </button>
      </form>
    </div>
  );
}

export default Checkout;
