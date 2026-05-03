import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import "../styles/dashboard.css";

// Foods are now fetched from the backend API

const categories = ["All", "Rice", "Grilled", "Salad", "Chicken", "Burger", "Noodles", "Drinks", "Curry"];

function Dashboard() {
  const navigate = useNavigate();
  const { addToCart, cart, fetchCart } = useCart();

  const [searchText, setSearchText] = useState("");
  const [activeCategory, setActiveCategory] = useState("All");
  const [added, setAdded] = useState({});
  const [foods, setFoods] = useState([]);

  React.useEffect(() => {
    // Fetch foods
    import("../services/api").then(({ default: API }) => {
      API.get("/foods")
        .then((res) => {
          if (res.data.success) {
            setFoods(res.data.data);
          }
        })
        .catch((err) => console.error("Failed to load foods", err));
    });

    // Fetch user's cart
    fetchCart();
  }, []);

  const cartCount = cart.reduce((sum, item) => sum + item.quantity, 0);
  const userName = localStorage.getItem("userName") || "there";

  const filtered = foods.filter((f) => {
    const matchCategory = activeCategory === "All" || f.category === activeCategory;
    const matchSearch = f.name.toLowerCase().includes(searchText.toLowerCase());
    return matchCategory && matchSearch;
  });

  const handleAdd = (food) => {
    addToCart(food);
    setAdded((prev) => ({ ...prev, [food.id]: true }));
    setTimeout(() => setAdded((prev) => ({ ...prev, [food.id]: false })), 700);
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userName");
    localStorage.removeItem("userEmail");
    navigate("/login");
  };

  return (
    <div className="dashboard">

      {/* HEADER */}
      <div className="dash-header">
        <div className="dash-menu">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="5" r="1"/><circle cx="12" cy="12" r="1"/><circle cx="12" cy="19" r="1"/></svg>
        </div>
        <h2 className="dash-title">Popular Food</h2>
        <div className="dash-actions">
          <Link to="/orders" className="cart-link" style={{ marginRight: '15px', color: '#6a1b9a' }} title="Order History">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="16" y1="13" x2="8" y2="13"></line><line x1="16" y1="17" x2="8" y2="17"></line><polyline points="10 9 9 9 8 9"></polyline></svg>
          </Link>
          <Link to="/cart" className="cart-link">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/></svg>
            {cartCount > 0 && <span className="cart-badge">{cartCount}</span>}
          </Link>
        </div>
      </div>

      {/* SEARCH BAR */}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Search..."
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
        />
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
      </div>

      {/* FOOD GRID */}
      <div className="food-grid">
        {filtered.length === 0 && (
          <p className="no-results">No food found 😕</p>
        )}

        {filtered.map((food) => (
          <div className="food-card" key={food.id}>

            <div className="heart">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>
            </div>

            <img src={food.img} alt={food.name} />

            <h3>{food.name}</h3>

            <p className="desc">{food.description}</p>

            <div className="card-bottom">
              <span className="food-price">P{food.price}</span>
              <button
                className={`add-btn ${added[food.id] ? "added" : ""}`}
                onClick={() => handleAdd(food)}
              >
                +
              </button>
            </div>

          </div>
        ))}
      </div>

    </div>
  );
}

export default Dashboard;