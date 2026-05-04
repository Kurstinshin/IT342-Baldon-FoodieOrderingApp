import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useCart } from "../cart/CartContext";
import "./dashboard.css";

function Dashboard() {
  const navigate = useNavigate();
  const { addToCart, cart, fetchCart } = useCart();

  const [searchText, setSearchText] = useState("");
  const [foods, setFoods] = useState([]);
  const [liked, setLiked] = useState({});
  const [menuOpen, setMenuOpen] = useState(false);

  useEffect(() => {
    import("../../shared/api/api").then(({ default: API }) => {
      API.get("/foods")
        .then((res) => {
          if (res.data.success) setFoods(res.data.data);
        })
        .catch((err) => console.error(err));
    });
    fetchCart();
  }, [fetchCart]);

  const cartCount = cart.reduce((sum, item) => sum + item.quantity, 0);

  const filtered = foods.filter((f) =>
    f.name.toLowerCase().includes(searchText.toLowerCase())
  );

  const toggleLike = (id) =>
    setLiked((prev) => ({ ...prev, [id]: !prev[id] }));

  return (
    <div className="dashboard">

      {/* ── HEADER ── */}
      <div className="dash-header">
        {/* Three-dot menu */}
        <div className="dash-menu" onClick={() => setMenuOpen(!menuOpen)}>
          <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor">
            <circle cx="12" cy="5" r="1.5"/>
            <circle cx="12" cy="12" r="1.5"/>
            <circle cx="12" cy="19" r="1.5"/>
          </svg>
          {menuOpen && (
            <div className="dash-dropdown">
              <div className="dash-dropdown-item" onClick={() => navigate("/orders")}>My Orders</div>
              <div className="dash-dropdown-item" onClick={() => { localStorage.clear(); navigate("/login"); }}>Logout</div>
            </div>
          )}
        </div>

        <h2 className="dash-title">Popular Food</h2>

        {/* Cart icon */}
        <Link to="/cart" className="cart-link">
          <svg width="26" height="26" viewBox="0 0 24 24" fill="none"
            stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="9" cy="21" r="1"/>
            <circle cx="20" cy="21" r="1"/>
            <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/>
          </svg>
          {cartCount > 0 && <span className="cart-badge">{cartCount}</span>}
        </Link>
      </div>

      {/* ── SEARCH BAR ── */}
      <div className="search-bar">
        <input
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          placeholder=""
        />
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none"
          stroke="#555" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
          <circle cx="11" cy="11" r="8"/>
          <line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
      </div>

      {/* ── FOOD GRID ── */}
      <div className="food-grid">
        {filtered.map((food) => (
          <div key={food.id} className="food-card">

            {/* Image + Heart */}
            <div className="card-img-wrap">
              <img src={food.img} alt={food.name} />
              <button
                className="heart-btn"
                onClick={() => toggleLike(food.id)}
              >
                {liked[food.id]
                  ? <svg width="18" height="18" viewBox="0 0 24 24" fill="#e74c3c" stroke="#e74c3c" strokeWidth="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>
                  : <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#bbb" strokeWidth="2"><path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"/></svg>
                }
              </button>
            </div>

            {/* Info */}
            <h3 className="food-name">{food.name}</h3>
            {food.description && (
              <p className="food-desc">{food.description}</p>
            )}

            {/* Price + Add */}
            <div className="card-bottom">
              <span className="food-price">P{food.price}</span>
              <button className="add-btn" onClick={() => addToCart(food)}>+</button>
            </div>

          </div>
        ))}
      </div>

    </div>
  );
}

export default Dashboard;
