import { createContext, useContext, useState, useEffect } from "react";
import API from "../../services/api";

const CartContext = createContext();

export const CartProvider = ({ children }) => {
  const [cart, setCart] = useState([]);

  // Fetch cart from backend API
  const fetchCart = async () => {
    const userId = localStorage.getItem("userId");
    if (!userId) return;

    try {
      const res = await API.get(`/cart/${userId}`);
      if (res.data.success) {
        // Map backend CartItem to match frontend expectation
        const formatted = res.data.data.map((item) => ({
          id: item.food.id,
          cartItemId: item.id,
          name: item.food.name,
          price: item.food.price,
          img: item.food.img,
          quantity: item.quantity,
        }));
        setCart(formatted);
      }
    } catch (err) {
      console.error("Failed to fetch cart", err);
    }
  };

  // Run once on mount if userId exists
  useEffect(() => {
    fetchCart();
  }, []);

  const addToCart = async (product) => {
    const userId = localStorage.getItem("userId");
    if (!userId) return;

    // Optimistic update
    setCart((prev) => {
      const existing = prev.find((item) => item.id === product.id);
      if (existing) {
        return prev.map((item) =>
          item.id === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item
        );
      }
      return [...prev, { ...product, quantity: 1 }];
    });

    try {
      await API.post(`/cart/${userId}`, {
        foodId: product.id,
        quantity: 1,
      });
      fetchCart(); // Refetch to get actual cartItemId
    } catch (err) {
      console.error("Failed to add to cart", err);
    }
  };

  const decreaseQty = async (id) => {
    const userId = localStorage.getItem("userId");
    if (!userId) return;

    const item = cart.find((i) => i.id === id);
    if (!item) return;

    // Optimistic update
    setCart((prev) =>
      prev
        .map((i) => (i.id === id ? { ...i, quantity: i.quantity - 1 } : i))
        .filter((i) => i.quantity > 0)
    );

    try {
      if (item.quantity > 1) {
        await API.put(`/cart/${userId}/${item.cartItemId}`, {
          quantity: item.quantity - 1,
        });
      } else {
        await API.delete(`/cart/${userId}/${item.cartItemId}`);
      }
      fetchCart();
    } catch (err) {
      console.error("Failed to decrease qty", err);
    }
  };

  const removeFromCart = async (id) => {
    const userId = localStorage.getItem("userId");
    if (!userId) return;

    const item = cart.find((i) => i.id === id);
    if (!item) return;

    setCart((prev) => prev.filter((i) => i.id !== id));

    try {
      await API.delete(`/cart/${userId}/${item.cartItemId}`);
      fetchCart();
    } catch (err) {
      console.error("Failed to remove from cart", err);
    }
  };

  const clearCart = async () => {
    const userId = localStorage.getItem("userId");
    if (!userId) return;

    setCart([]);
    try {
      await API.delete(`/cart/${userId}`);
    } catch (err) {
      console.error("Failed to clear cart", err);
    }
  };

  return (
    <CartContext.Provider
      value={{
        cart,
        addToCart,
        decreaseQty,
        removeFromCart,
        clearCart,
        fetchCart,
      }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => useContext(CartContext);