package edu.cit.baldon.foodiemobile.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cit.baldon.foodiemobile.shared.api.ApiClient
import edu.cit.baldon.foodiemobile.shared.model.AddToCartRequest
import edu.cit.baldon.foodiemobile.shared.model.CartItem
import edu.cit.baldon.foodiemobile.shared.model.Food
import edu.cit.baldon.foodiemobile.shared.model.UpdateCartRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart = _cart.asStateFlow()

    val totalAmount: Double
        get() = _cart.value.sumOf { it.food.price * it.quantity }

    fun fetchCart(userId: Long) {
        viewModelScope.launch {
            try {
                val res = ApiClient.service.getCart(userId)
                if (res.success) _cart.value = res.data ?: emptyList()
            } catch (_: Exception) {}
        }
    }

    fun addToCart(userId: Long, food: Food) {
        viewModelScope.launch {
            try {
                ApiClient.service.addToCart(userId, AddToCartRequest(food.id, 1))
                fetchCart(userId)
            } catch (_: Exception) {}
        }
    }

    fun increaseQty(userId: Long, item: CartItem) {
        viewModelScope.launch {
            try {
                ApiClient.service.updateCartItem(userId, item.id, UpdateCartRequest(item.quantity + 1))
                fetchCart(userId)
            } catch (_: Exception) {}
        }
    }

    fun decreaseQty(userId: Long, item: CartItem) {
        viewModelScope.launch {
            try {
                if (item.quantity > 1) {
                    ApiClient.service.updateCartItem(userId, item.id, UpdateCartRequest(item.quantity - 1))
                } else {
                    ApiClient.service.removeCartItem(userId, item.id)
                }
                fetchCart(userId)
            } catch (_: Exception) {}
        }
    }

    val cartCount: Int get() = _cart.value.sumOf { it.quantity }
}
