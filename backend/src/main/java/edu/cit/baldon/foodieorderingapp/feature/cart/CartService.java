package edu.cit.baldon.foodieorderingapp.feature.cart;

import edu.cit.baldon.foodieorderingapp.feature.auth.User;
import edu.cit.baldon.foodieorderingapp.feature.auth.UserRepository;
import edu.cit.baldon.foodieorderingapp.feature.food.Food;
import edu.cit.baldon.foodieorderingapp.feature.food.FoodRepository;
import edu.cit.baldon.foodieorderingapp.shared.ApiResponse;
import edu.cit.baldon.foodieorderingapp.shared.SliceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Cart Feature — Service
 */
@Service
public class CartService {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private UserRepository     userRepository;
    @Autowired private FoodRepository     foodRepository;

    public ApiResponse<List<CartItem>> getCart(Long userId) {
        return SliceUtils.ok(cartItemRepository.findByUserId(userId), "Cart fetched successfully");
    }

    public ApiResponse<CartItem> addToCart(Long userId, CartRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        Food food = foodRepository.findById(request.getFoodId()).orElse(null);

        if (user == null || food == null) {
            return SliceUtils.notFound("User or Food");
        }

        return cartItemRepository.findByUserIdAndFoodId(userId, request.getFoodId())
            .map(item -> {
                item.setQuantity(item.getQuantity() + request.getQuantity());
                return SliceUtils.ok(cartItemRepository.save(item), "Cart updated successfully");
            })
            .orElseGet(() -> {
                CartItem newItem = new CartItem(user, food, request.getQuantity());
                ApiResponse<CartItem> r = SliceUtils.ok(cartItemRepository.save(newItem), "Item added to cart");
                r.setStatus(201);
                return r;
            });
    }

    public ApiResponse<CartItem> updateQuantity(Long userId, Long itemId, CartRequest request) {
        return cartItemRepository.findById(itemId)
            .filter(item -> item.getUser().getId().equals(userId))
            .map(item -> {
                item.setQuantity(request.getQuantity());
                return SliceUtils.ok(cartItemRepository.save(item), "Quantity updated");
            })
            .orElse(SliceUtils.notFound("Cart item"));
    }

    public ApiResponse<Void> removeItem(Long userId, Long itemId) {
        return cartItemRepository.findById(itemId)
            .filter(item -> item.getUser().getId().equals(userId))
            .map(item -> {
                cartItemRepository.delete(item);
                return SliceUtils.<Void>ok(null, "Item removed from cart");
            })
            .orElse(SliceUtils.notFound("Cart item"));
    }

    @Transactional
    public ApiResponse<Void> clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
        return SliceUtils.ok(null, "Cart cleared successfully");
    }
}
