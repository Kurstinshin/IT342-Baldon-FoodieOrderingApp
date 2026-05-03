import React from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import "../styles/cart.css";

function Cart() {
  const navigate = useNavigate();
  const { cart, addToCart, decreaseQty, removeFromCart } = useCart();

  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

  return (
    <div className="cart-mobile">

      {/* TOP BAR */}
      <div className="cart-topbar">
        <span className="icon back" onClick={() => navigate(-1)}>
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"><path d="M15 18l-6-6 6-6"/></svg>
        </span>
        <span className="icon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/></svg>
        </span>
      </div>

      {/* TITLE */}
      <h2 className="my-order">My Order</h2>

      {/* EMPTY STATE */}
      {cart.length === 0 && (
        <div className="empty-cart">
          <p>🍽️ Your cart is empty</p>
          <button className="back-btn" onClick={() => navigate("/dashboard")}>
            Browse Food
          </button>
        </div>
      )}

      {/* ITEMS */}
      {cart.map((item) => (
        <div className="cart-item" key={item.id}>
          <div className="img-container">
            <img src={item.img} alt={item.name} />
          </div>

          <div className="item-info">
            <div className="row">
              <h4>{item.name}</h4>
              <span className="price">P{item.price * item.quantity}</span>
            </div>

            <div className="qty">
              <button onClick={() => addToCart(item)}>+</button>
              <span>{item.quantity}</span>
              <button onClick={() => decreaseQty(item.id)}>-</button>
            </div>
          </div>
        </div>
      ))}

      {/* TOTAL AND CHECKOUT */}
      {cart.length > 0 && (
        <div className="cart-bottom">
          <div className="cart-total">
            <span>Total = P{total}</span>
          </div>

          <button
            className="checkout-btn"
            onClick={() => navigate("/checkout")}
          >
            Checkout
          </button>
        </div>
      )}

    </div>
  );
}

export default Cart;