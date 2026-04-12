import { useEffect, useState } from "react";

function Cart() {

  const [cart, setCart] = useState([]);

  useEffect(() => {
    const data = JSON.parse(localStorage.getItem("cart")) || [];
    setCart(data);
  }, []);

  const total = cart.reduce((sum, item) => sum + item.price, 0);

  return (
    <div>

      <h2>🛒 Shopping Cart</h2>

      {cart.map((item, index) => (
        <div key={index}>
          <h4>{item.name}</h4>
          <p>₱{item.price}</p>
        </div>
      ))}

      <h3>Total: ₱{total}</h3>

    </div>
  );
}

export default Cart;