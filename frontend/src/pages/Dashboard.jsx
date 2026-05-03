import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import "../styles/dashboard.css";

const categories = ["All", "Rice", "Grilled", "Salad", "Chicken", "Burger", "Noodles", "Drinks", "Curry"];

function Dashboard() {
  const navigate = useNavigate();
  const { addToCart, cart, fetchCart } = useCart();

  const [searchText, setSearchText] = useState("");
  const [activeCategory, setActiveCategory] = useState("All");
  const [added, setAdded] = useState({});
  const [foods, setFoods] = useState([]);

  useEffect(() => {
    import("../services/api").then(({ default: API }) => {
      API.get("/foods")
        .then((res) => {
          if (res.data.success) setFoods(res.data.data);
        })
        .catch((err) => console.error(err));
    });

    fetchCart();
  }, [fetchCart]);

  const cartCount = cart.reduce((sum, item) => sum + item.quantity, 0);

  const filtered = foods.filter((f) => {
    const matchCategory = activeCategory === "All" || f.category === activeCategory;
    const matchSearch = f.name.toLowerCase().includes(searchText.toLowerCase());
    return matchCategory && matchSearch;
  });

  const handleAdd = (food) => {
    addToCart(food);
    setAdded((prev) => ({ ...prev, [food.id]: true }));

    setTimeout(() => {
      setAdded((prev) => ({ ...prev, [food.id]: false }));
    }, 700);
  };

  return (
    <div className="dashboard">

      {/* HEADER */}
      <div className="dash-header">
        <h2 className="dash-title">Popular Food</h2>

        <div className="dash-actions">
          <Link to="/cart">
            🛒 {cartCount > 0 && <span>{cartCount}</span>}
          </Link>
        </div>
      </div>

      {/* SEARCH */}
      <input
        value={searchText}
        onChange={(e) => setSearchText(e.target.value)}
        placeholder="Search food..."
      />

      {/* FOOD LIST */}
      <div className="food-grid">
        {filtered.map((food) => (
          <div key={food.id} className="food-card">
            <img src={food.img} alt={food.name} />
            <h3>{food.name}</h3>
            <p>P{food.price}</p>

            <button onClick={() => handleAdd(food)}>
              +
            </button>
          </div>
        ))}
      </div>

    </div>
  );
}

export default Dashboard;