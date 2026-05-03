import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import API from "../services/api";
import "../styles/cart.css"; 

function OrderHistory() {
  const navigate = useNavigate();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
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

    fetchOrders();
  }, []);

  return (
    <div className="cart-mobile" style={{ padding: "20px" }}>
      {/* TOP BAR */}
      <div className="cart-topbar" style={{ marginBottom: "20px" }}>
        <span className="icon back" onClick={() => navigate("/dashboard")} style={{ cursor: "pointer" }}>
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"><path d="M15 18l-6-6 6-6"/></svg>
        </span>
        <h2>Order History</h2>
        <span style={{ width: "24px" }}></span>
      </div>

      {loading ? (
        <p>Loading orders...</p>
      ) : orders.length === 0 ? (
        <div className="empty-cart">
          <p>🍽️ You have no past orders</p>
          <button className="back-btn" onClick={() => navigate("/dashboard")}>
            Browse Food
          </button>
        </div>
      ) : (
        <div className="orders-list">
          {orders.map((order) => (
            <div key={order.id} style={{ border: "1px solid #eee", borderRadius: "8px", padding: "15px", marginBottom: "15px", background: "#fff", color: "#333" }}>
              <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "10px" }}>
                <strong>Order #{order.id}</strong>
                <span style={{ 
                  background: order.status === "PENDING" ? "#ffeaa7" : "#55efc4", 
                  padding: "4px 8px", 
                  borderRadius: "4px",
                  fontSize: "12px",
                  fontWeight: "bold"
                }}>
                  {order.status}
                </span>
              </div>
              <p style={{ margin: "5px 0", fontSize: "14px", color: "#555" }}>Customer: {order.customerName}</p>
              
              <div style={{ marginTop: "10px", padding: "10px", background: "#f9f9f9", borderRadius: "4px" }}>
                {order.items && order.items.map((item) => (
                  <div key={item.id} style={{ display: "flex", justifyContent: "space-between", fontSize: "13px", padding: "4px 0", borderBottom: "1px dashed #ccc" }}>
                    <span>{item.quantity}x {item.food.name}</span>
                    <span>P{item.food.price * item.quantity}</span>
                  </div>
                ))}
              </div>
              
              <div style={{ marginTop: "10px", textAlign: "right", fontWeight: "bold", fontSize: "16px" }}>
                Total: P{order.totalAmount}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default OrderHistory;
