package edu.cit.baldon.foodieorderingapp.feature.order;

import edu.cit.baldon.foodieorderingapp.shared.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Order Feature — Controller
 * Routes: GET/POST/PATCH /api/v1/orders
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<Order>> placeOrder(
            @PathVariable Long userId, @RequestBody OrderRequest request) {
        ApiResponse<Order> r = orderService.placeOrder(userId, request);
        int status = r.getStatus() != null ? r.getStatus() : (r.isSuccess() ? 201 : 400);
        return ResponseEntity.status(status).body(r);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Order>>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getByUser(userId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<Order>> updateStatus(
            @PathVariable Long orderId, @RequestBody Map<String, String> body) {
        ApiResponse<Order> r = orderService.updateStatus(orderId, body);
        return r.isSuccess() ? ResponseEntity.ok(r) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(r);
    }
}
