import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import API from "../services/api";

function AdminDashboard() {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState("products");
  
  const [foods, setFoods] = useState([]);
  const [orders, setOrders] = useState([]);
  
  // For adding/editing food
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [foodForm, setFoodForm] = useState({
    name: "",
    description: "",
    price: "",
    category: "",
    img: ""
  });

  const fetchFoods = async () => {
    try {
      const res = await API.get("/foods");
      if (res.data.success) {
        setFoods(res.data.data);
      }
    } catch (err) {
      console.error(err);
    }
  };

  const fetchOrders = async () => {
    try {
      const res = await API.get("/orders");
      if (res.data.success) {
        setOrders(res.data.data);
      }
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchFoods();
    fetchOrders();
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

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
      alert("Failed to save food");
    }
  };

  const deleteFood = async (id) => {
    if (window.confirm("Delete this food?")) {
      try {
        await API.delete(`/foods/${id}`);
        fetchFoods();
      } catch (err) {
        alert("Failed to delete");
      }
    }
  };

  const editFood = (food) => {
    setFoodForm({
      name: food.name,
      description: food.description,
      price: food.price,
      category: food.category,
      img: food.img
    });
    setEditingId(food.id);
    setShowForm(true);
  };

  return (
    <div style={{ padding: "20px", maxWidth: "1200px", margin: "0 auto", fontFamily: "sans-serif" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px" }}>
        <h2>Admin Panel</h2>
        <button onClick={handleLogout} style={{ padding: "8px 16px", background: "#ff4757", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>
          Logout
        </button>
      </div>

      <div style={{ marginBottom: "20px" }}>
        <button 
          onClick={() => setActiveTab("products")}
          style={{ padding: "10px 20px", marginRight: "10px", background: activeTab === "products" ? "#6a1b9a" : "#eee", color: activeTab === "products" ? "white" : "black", border: "none", borderRadius: "4px", cursor: "pointer" }}
        >
          Manage Products
        </button>
        <button 
          onClick={() => setActiveTab("orders")}
          style={{ padding: "10px 20px", background: activeTab === "orders" ? "#6a1b9a" : "#eee", color: activeTab === "orders" ? "white" : "black", border: "none", borderRadius: "4px", cursor: "pointer" }}
        >
          View All Orders
        </button>
      </div>

      {activeTab === "products" && (
        <div>
          <button onClick={() => { setShowForm(!showForm); setEditingId(null); setFoodForm({name: "", description: "", price: "", category: "", img: ""}); }} style={{ marginBottom: "20px", padding: "10px", background: "#2ed573", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>
            {showForm ? "Close Form" : "+ Add New Food"}
          </button>

          {showForm && (
            <form onSubmit={submitFood} style={{ background: "#f1f2f6", padding: "20px", borderRadius: "8px", marginBottom: "20px" }}>
              <div style={{ marginBottom: "10px" }}><input name="name" value={foodForm.name} onChange={handleFoodFormChange} placeholder="Name" required style={{ width: "100%", padding: "8px" }} /></div>
              <div style={{ marginBottom: "10px" }}><input name="description" value={foodForm.description} onChange={handleFoodFormChange} placeholder="Description" required style={{ width: "100%", padding: "8px" }} /></div>
              <div style={{ marginBottom: "10px" }}><input type="number" name="price" value={foodForm.price} onChange={handleFoodFormChange} placeholder="Price" required style={{ width: "100%", padding: "8px" }} /></div>
              <div style={{ marginBottom: "10px" }}><input name="category" value={foodForm.category} onChange={handleFoodFormChange} placeholder="Category" required style={{ width: "100%", padding: "8px" }} /></div>
              <div style={{ marginBottom: "10px" }}><input name="img" value={foodForm.img} onChange={handleFoodFormChange} placeholder="Image URL" required style={{ width: "100%", padding: "8px" }} /></div>
              <button type="submit" style={{ padding: "10px 20px", background: "#1e90ff", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Save</button>
            </form>
          )}

          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ background: "#eee", textAlign: "left" }}>
                <th style={{ padding: "10px", borderBottom: "2px solid #ddd" }}>Image</th>
                <th style={{ padding: "10px", borderBottom: "2px solid #ddd" }}>Name</th>
                <th style={{ padding: "10px", borderBottom: "2px solid #ddd" }}>Price</th>
                <th style={{ padding: "10px", borderBottom: "2px solid #ddd" }}>Category</th>
                <th style={{ padding: "10px", borderBottom: "2px solid #ddd" }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {foods.map(f => (
                <tr key={f.id} style={{ borderBottom: "1px solid #ddd" }}>
                  <td style={{ padding: "10px" }}><img src={f.img} alt={f.name} style={{ width: "50px", height: "50px", objectFit: "cover", borderRadius: "4px" }} /></td>
                  <td style={{ padding: "10px" }}>{f.name}</td>
                  <td style={{ padding: "10px" }}>P{f.price}</td>
                  <td style={{ padding: "10px" }}>{f.category}</td>
                  <td style={{ padding: "10px" }}>
                    <button onClick={() => editFood(f)} style={{ marginRight: "10px", padding: "5px 10px", background: "#f39c12", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Edit</button>
                    <button onClick={() => deleteFood(f.id)} style={{ padding: "5px 10px", background: "#e74c3c", color: "white", border: "none", borderRadius: "4px", cursor: "pointer" }}>Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {activeTab === "orders" && (
        <div>
          <h3>All Customer Orders</h3>
          {orders.length === 0 ? <p>No orders found.</p> : (
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr style={{ background: "#eee", textAlign: "left" }}>
                  <th style={{ padding: "10px", borderBottom: "2px solid #ddd" }}>Order ID</th>
                  <th style={{ padding: "10px", borderBottom: "2px solid #ddd" }}>Customer Name</th>
                  <th style={{ padding: "10px", borderBottom: "2px solid #ddd" }}>Total Amount</th>
                  <th style={{ padding: "10px", borderBottom: "2px solid #ddd" }}>Items</th>
                  <th style={{ padding: "10px", borderBottom: "2px solid #ddd" }}>Status</th>
                </tr>
              </thead>
              <tbody>
                {orders.map(o => (
                  <tr key={o.id} style={{ borderBottom: "1px solid #ddd" }}>
                    <td style={{ padding: "10px" }}>#{o.id}</td>
                    <td style={{ padding: "10px" }}>{o.customerName}</td>
                    <td style={{ padding: "10px" }}>P{o.totalAmount}</td>
                    <td style={{ padding: "10px" }}>
                      <ul style={{ margin: 0, paddingLeft: "20px" }}>
                        {o.items.map(item => (
                          <li key={item.id}>{item.quantity}x {item.food.name}</li>
                        ))}
                      </ul>
                    </td>
                    <td style={{ padding: "10px", fontWeight: "bold", color: o.status === "PENDING" ? "#f39c12" : "#27ae60" }}>
                      {o.status}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}

export default AdminDashboard;
