import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import API from "../services/api";
import "../styles/checkout.css";

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

  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
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
        await fetchCart(); // Sync frontend cart state (should be empty now)
        setPlaced(true);
      }
    } catch (err) {
      console.error("Checkout failed:", err);
      alert("Checkout failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  if (placed) {
    return (
      <div className="checkout-page">
        <div className="order-success">
          <div className="success-icon">🎉</div>
          <h2>Order Placed!</h2>
          <p>Thank you, <strong>{form.name}</strong>! Your food is being prepared.</p>
          <p>Delivering to: <strong>{form.address}</strong></p>
          <button className="done-btn" onClick={() => navigate("/dashboard")}>
            Order More Food
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="checkout-page">
      {/* HEADER */}
      <div className="checkout-header">
        <span className="back-icon" onClick={() => navigate("/cart")}>
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"><path d="M15 18l-6-6 6-6"/></svg>
        </span>
        <h2>Payment</h2>
        <span style={{ width: 24 }}></span>
      </div>

      {/* PROGRESS BAR */}
      <div className="progress-bar-container">
        <div className="progress-line"></div>
        <div className="progress-steps">
          <div className="step-circle active"><svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="4" strokeLinecap="round" strokeLinejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg></div>
          <div className="step-circle active"><svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="4" strokeLinecap="round" strokeLinejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg></div>
          <div className="step-circle active"><svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="4" strokeLinecap="round" strokeLinejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg></div>
        </div>
      </div>

      {/* PAYMENT FORM */}
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
              required 
            />
          </div>
        </div>

        <div className="form-group">
          <label>Cardholder Name</label>
          <div className="input-wrapper">
            <span className="input-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="#fbd65b" stroke="#6a1b9a" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
            </span>
            <input 
              type="text" 
              name="name"
              value={form.name}
              onChange={handleChange}
              className="checkout-input" 
              required 
            />
          </div>
        </div>

        <div className="form-group">
          <label>Card Number</label>
          <div className="input-wrapper">
            <span className="input-icon">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="#fbd65b" stroke="#6a1b9a" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"></rect><line x1="1" y1="10" x2="23" y2="10"></line></svg>
            </span>
            <input 
              type="text" 
              name="cardNumber"
              value={form.cardNumber}
              onChange={handleChange}
              className="checkout-input" 
              required 
            />
          </div>
        </div>

        <div className="divider"></div>

        <div className="amount-payable">
          <span>Amount Payable</span>
          <span className="amount-total">P{total}</span>
        </div>

        <button className="place-order-btn" type="submit" disabled={loading}>
          {loading ? "Processing..." : "Pay Now"}
        </button>
      </form>
    </div>
  );
}

export default Checkout;