package edu.cit.baldon.foodieorderingapp.feature.cart;

import edu.cit.baldon.foodieorderingapp.shared.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cart Feature — Controller
 * Routes: GET/POST/PUT/DELETE /api/v1/cart/{userId}
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<CartItem>> addToCart(
            @PathVariable Long userId, @RequestBody CartRequest request) {
        ApiResponse<CartItem> r = cartService.addToCart(userId, request);
        int status = r.getStatus() != null ? r.getStatus() : (r.isSuccess() ? 200 : 404);
        return ResponseEntity.status(status).body(r);
    }

    @PutMapping("/{userId}/{itemId}")
    public ResponseEntity<ApiResponse<CartItem>> updateQuantity(
            @PathVariable Long userId, @PathVariable Long itemId, @RequestBody CartRequest request) {
        ApiResponse<CartItem> r = cartService.updateQuantity(userId, itemId, request);
        return r.isSuccess() ? ResponseEntity.ok(r) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
    }

    @DeleteMapping("/{userId}/{itemId}")
    public ResponseEntity<ApiResponse<Void>> removeItem(
            @PathVariable Long userId, @PathVariable Long itemId) {
        ApiResponse<Void> r = cartService.removeItem(userId, itemId);
        return r.isSuccess() ? ResponseEntity.ok(r) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }
}
