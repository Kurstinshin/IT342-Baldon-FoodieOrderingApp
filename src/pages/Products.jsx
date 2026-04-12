
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const foods = [
  { id: 1, name: "Burger", price: 50, img: "https://images.unsplash.com/photo-1550547660-d9450f859349" },
  { id: 2, name: "Pizza", price: 50, img: "https://images.unsplash.com/photo-1548365328-9f547c4f7e4b" },
  { id: 3, name: "Fries", price: 50, img: "https://images.unsplash.com/photo-1573080496219-bb080dd4f877" },
  { id: 4, name: "Chicken", price: 50, img: "https://images.unsplash.com/photo-1604908177522-0406d9bfa3e0" },
  { id: 5, name: "Pasta", price: 50, img: "https://images.unsplash.com/photo-1525755662778-989d0524087e" },
  { id: 6, name: "Sushi", price: 50, img: "https://images.unsplash.com/photo-1553621042-f6e147245754" },
];

function Products() {
  const navigate = useNavigate();
  const [search, setSearch] = useState([]);

  const filtered = foods.filter(f =>
    f.name.toLowerCase().includes(search.toLowerCase())
  );

  const addToCart = (item) => {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    cart.push(item);
    localStorage.setItem("cart", JSON.stringify(cart));
    alert("Added to cart 🛒");
  };

  return (
    <div>

      {/* NAVBAR */}
      <div className="navbar">
        <h2>🍔 Popular Food</h2>

        <button className="btn" onClick={() => navigate("/cart")}>
          Cart
        </button>
      </div>

      <div className="container">

        {/* SEARCH */}
        <input
          className="search"
          placeholder="Search food..."
          onChange={(e) => setSearch(e.target.value)}
        />

        {/* GRID */}
        <div className="grid">

          {filtered.map(food => (
            <div className="card" key={food.id}>

              <img src={food.img} />

              <h3>{food.name}</h3>
              <p className="price">₱{food.price}</p>

              <button className="btn" onClick={() => addToCart(food)}>
                Add to Cart
              </button>

            </div>
          ))}

        </div>

      </div>
    </div>
  );
}

export default Products;