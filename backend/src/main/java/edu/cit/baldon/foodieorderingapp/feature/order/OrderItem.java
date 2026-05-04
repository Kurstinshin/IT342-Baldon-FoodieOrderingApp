package edu.cit.baldon.foodieorderingapp.feature.order;

import edu.cit.baldon.foodieorderingapp.feature.food.Food;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(nullable = false)
    private Integer quantity;

    public OrderItem() {}

    public OrderItem(Order order, Food food, Integer quantity) {
        this.order    = order;
        this.food     = food;
        this.quantity = quantity;
    }

    public Long    getId()       { return id; }
    public void    setId(Long v) { this.id = v; }
    public Order   getOrder()    { return order; }
    public void    setOrder(Order v)   { this.order = v; }
    public Food    getFood()     { return food; }
    public void    setFood(Food v)     { this.food = v; }
    public Integer getQuantity() { return quantity; }
    public void    setQuantity(Integer v) { this.quantity = v; }
}
