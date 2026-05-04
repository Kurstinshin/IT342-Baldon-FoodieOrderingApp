package edu.cit.baldon.foodieorderingapp.feature.order;

import edu.cit.baldon.foodieorderingapp.feature.auth.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String status; // PENDING | COMPLETED | CANCELLED

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Order(User user, String customerName, Double totalAmount, String status) {
        this.user         = user;
        this.customerName = customerName;
        this.totalAmount  = totalAmount;
        this.status       = status;
    }

    public Long         getId()           { return id; }
    public User         getUser()         { return user; }
    public void         setUser(User u)   { this.user = u; }
    public String       getCustomerName() { return customerName; }
    public void         setCustomerName(String v) { this.customerName = v; }
    public Double       getTotalAmount()  { return totalAmount; }
    public void         setTotalAmount(Double v)  { this.totalAmount = v; }
    public String       getStatus()       { return status; }
    public void         setStatus(String v)       { this.status = v; }
    public List<OrderItem> getItems()     { return items; }
    public void         setItems(List<OrderItem> v) { this.items = v; }
}
