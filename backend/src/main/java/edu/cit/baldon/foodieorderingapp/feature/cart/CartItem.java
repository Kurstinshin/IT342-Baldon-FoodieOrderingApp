package edu.cit.baldon.foodieorderingapp.feature.cart;

import edu.cit.baldon.foodieorderingapp.feature.auth.User;
import edu.cit.baldon.foodieorderingapp.feature.food.Food;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    private Integer quantity;

    public CartItem() {}

    public CartItem(User user, Food food, Integer quantity) {
        this.user     = user;
        this.food     = food;
        this.quantity = quantity;
    }

    public Long    getId()       { return id; }
    public User    getUser()     { return user; }
    public void    setUser(User u)     { this.user = u; }
    public Food    getFood()     { return food; }
    public void    setFood(Food f)     { this.food = f; }
    public Integer getQuantity() { return quantity; }
    public void    setQuantity(Integer q) { this.quantity = q; }
}
