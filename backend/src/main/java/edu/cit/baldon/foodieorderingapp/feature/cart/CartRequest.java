package edu.cit.baldon.foodieorderingapp.feature.cart;

public class CartRequest {
    private Long    foodId;
    private Integer quantity;

    public Long    getFoodId()   { return foodId; }
    public void    setFoodId(Long v)   { this.foodId = v; }
    public Integer getQuantity() { return quantity; }
    public void    setQuantity(Integer v) { this.quantity = v; }
}
