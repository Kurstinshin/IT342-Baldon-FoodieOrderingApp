package edu.cit.baldon.foodieorderingapp.feature.order;

import edu.cit.baldon.foodieorderingapp.feature.auth.User;
import edu.cit.baldon.foodieorderingapp.feature.auth.UserRepository;
import edu.cit.baldon.foodieorderingapp.feature.cart.CartItem;
import edu.cit.baldon.foodieorderingapp.feature.cart.CartItemRepository;
import edu.cit.baldon.foodieorderingapp.shared.ApiResponse;
import edu.cit.baldon.foodieorderingapp.shared.SliceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Order Feature — Service
 */
@Service
public class OrderService {

    @Autowired private OrderRepository     orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private CartItemRepository  cartItemRepository;
    @Autowired private UserRepository      userRepository;

    @Transactional
    public ApiResponse<Order> placeOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return SliceUtils.notFound("User");

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) return SliceUtils.fail("Cart is empty", "CART-001", "No items in cart");

        double total = cartItems.stream()
            .mapToDouble(i -> i.getFood().getPrice() * i.getQuantity())
            .sum();

        Order order = new Order(user, request.getCustomerName(), total, "PENDING");
        Order saved = orderRepository.save(order);

        List<OrderItem> items = cartItems.stream()
            .map(ci -> orderItemRepository.save(new OrderItem(saved, ci.getFood(), ci.getQuantity())))
            .collect(Collectors.toList());

        saved.setItems(items);
        cartItemRepository.deleteAll(cartItems);

        ApiResponse<Order> r = SliceUtils.ok(saved, "Order placed successfully");
        r.setStatus(201);
        return r;
    }

    public ApiResponse<List<Order>> getByUser(Long userId) {
        return SliceUtils.ok(orderRepository.findByUserId(userId), "Orders fetched successfully");
    }

    public ApiResponse<List<Order>> getAll() {
        return SliceUtils.ok(orderRepository.findAll(), "All orders fetched successfully");
    }

    public ApiResponse<Order> updateStatus(Long orderId, Map<String, String> body) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return SliceUtils.notFound("Order");

        String newStatus = body.get("status");
        if (newStatus == null || newStatus.isBlank())
            return SliceUtils.fail("Status is required", "VAL-001", "Provide status field");

        order.setStatus(newStatus.toUpperCase());
        return SliceUtils.ok(orderRepository.save(order), "Order status updated to " + newStatus.toUpperCase());
    }
}
