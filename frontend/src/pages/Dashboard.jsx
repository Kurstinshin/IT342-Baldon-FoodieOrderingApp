import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { useCart } from "../context/CartContext";
import "../styles/dashboard.css";

function Dashboard() {
  const { addToCart, cart, fetchCart } = useCart();

  const [searchText, setSearchText] = useState("");
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

  const filtered = foods.filter((f) =>
    f.name.toLowerCase().includes(searchText.toLowerCase())
  );

  const handleAdd = (food) => {
    addToCart(food);
  };

  return (
    <div className="dashboard">

      <div className="dash-header">
        <h2>Popular Food</h2>

        <Link to="/cart">
          🛒 {cartCount > 0 && <span>{cartCount}</span>}
        </Link>
      </div>

      <input
        value={searchText}
        onChange={(e) => setSearchText(e.target.value)}
        placeholder="Search food..."
      />

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