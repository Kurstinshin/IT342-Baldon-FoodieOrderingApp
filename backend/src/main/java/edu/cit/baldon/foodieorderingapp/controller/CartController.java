package edu.cit.baldon.foodieorderingapp.controller;

import edu.cit.baldon.foodieorderingapp.dto.ApiResponse;
import edu.cit.baldon.foodieorderingapp.dto.CartRequest;
import edu.cit.baldon.foodieorderingapp.entity.CartItem;
import edu.cit.baldon.foodieorderingapp.entity.Food;
import edu.cit.baldon.foodieorderingapp.entity.User;
import edu.cit.baldon.foodieorderingapp.repository.CartItemRepository;
import edu.cit.baldon.foodieorderingapp.repository.FoodRepository;
import edu.cit.baldon.foodieorderingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    private String getTimestamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC).format(Instant.now());
    }

    private <T> ApiResponse<T> createResponse(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setTimestamp(getTimestamp());
        return response;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCart(@PathVariable Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return ResponseEntity.ok(createResponse(cartItems, "Cart fetched successfully"));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<CartItem>> addToCart(@PathVariable Long userId, @RequestBody CartRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        Food food = foodRepository.findById(request.getFoodId()).orElse(null);

        if (user == null || food == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(null, "User or Food not found"));
        }

        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndFoodId(userId, request.getFoodId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
            return ResponseEntity.ok(createResponse(item, "Cart updated successfully"));
        } else {
            CartItem newItem = new CartItem(user, food, request.getQuantity());
            cartItemRepository.save(newItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(newItem, "Item added to cart"));
        }
    }

    @PutMapping("/{userId}/{itemId}")
    public ResponseEntity<ApiResponse<CartItem>> updateQuantity(@PathVariable Long userId, @PathVariable Long itemId, @RequestBody CartRequest request) {
        return cartItemRepository.findById(itemId)
                .filter(item -> item.getUser().getId().equals(userId))
                .map(item -> {
                    item.setQuantity(request.getQuantity());
                    cartItemRepository.save(item);
                    return ResponseEntity.ok(createResponse(item, "Quantity updated"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(null, "Cart item not found")));
    }

    @DeleteMapping("/{userId}/{itemId}")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(@PathVariable Long userId, @PathVariable Long itemId) {
        return cartItemRepository.findById(itemId)
                .filter(item -> item.getUser().getId().equals(userId))
                .map(item -> {
                    cartItemRepository.delete(item);
                    return ResponseEntity.ok(createResponse((Void) null, "Item removed from cart"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(null, "Cart item not found")));
    }
    
    @Transactional
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long userId) {
        cartItemRepository.deleteByUserId(userId);
        return ResponseEntity.ok(createResponse((Void) null, "Cart cleared successfully"));
    }
}
