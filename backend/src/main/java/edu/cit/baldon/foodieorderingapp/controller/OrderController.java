package edu.cit.baldon.foodieorderingapp.controller;

import edu.cit.baldon.foodieorderingapp.dto.ApiResponse;
import edu.cit.baldon.foodieorderingapp.dto.OrderRequest;
import edu.cit.baldon.foodieorderingapp.entity.CartItem;
import edu.cit.baldon.foodieorderingapp.entity.Order;
import edu.cit.baldon.foodieorderingapp.entity.OrderItem;
import edu.cit.baldon.foodieorderingapp.entity.User;
import edu.cit.baldon.foodieorderingapp.repository.CartItemRepository;
import edu.cit.baldon.foodieorderingapp.repository.OrderItemRepository;
import edu.cit.baldon.foodieorderingapp.repository.OrderRepository;
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
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

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

    private <T> ApiResponse<T> createErrorResponse(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setTimestamp(getTimestamp());
        return response;
    }

    // ── Place Order (User) ────────────────────────────────────────────
    @PostMapping("/{userId}")
    @Transactional
    public ResponseEntity<ApiResponse<Order>> placeOrder(
            @PathVariable Long userId,
            @RequestBody OrderRequest request) {

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("User not found"));
        }

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Cart is empty"));
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getFood().getPrice() * item.getQuantity())
                .sum();

        Order order = new Order(user, request.getCustomerName(), totalAmount, "PENDING");
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem(savedOrder, cartItem.getFood(), cartItem.getQuantity());
            return orderItemRepository.save(orderItem);
        }).collect(Collectors.toList());

        savedOrder.setItems(orderItems);
        cartItemRepository.deleteAll(cartItems);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createResponse(savedOrder, "Order placed successfully"));
    }

    // ── Get Orders by User ────────────────────────────────────────────
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrders(@PathVariable Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return ResponseEntity.ok(createResponse(orders, "Orders fetched successfully"));
    }

    // ── Admin: Get All Orders ─────────────────────────────────────────
    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(createResponse(orders, "All orders fetched successfully"));
    }

    // ── Admin: Update Order Status (PENDING / COMPLETED / CANCELLED) ──
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> body) {

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Order not found"));
        }

        String newStatus = body.get("status");
        if (newStatus == null || newStatus.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Status is required"));
        }

        order.setStatus(newStatus.toUpperCase());
        Order updated = orderRepository.save(order);
        return ResponseEntity.ok(createResponse(updated, "Order status updated to " + updated.getStatus()));
    }
}
