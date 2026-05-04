package edu.cit.baldon.foodiemobile.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cit.baldon.foodiemobile.shared.api.ApiClient
import edu.cit.baldon.foodiemobile.shared.model.Order
import edu.cit.baldon.foodiemobile.shared.model.OrderRequest
import edu.cit.baldon.foodiemobile.shared.model.UpdateStatusRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _placedOrder = MutableStateFlow<Order?>(null)
    val placedOrder = _placedOrder.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // User: fetch own orders
    fun fetchOrders(userId: Long) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val res = ApiClient.service.getOrdersByUser(userId)
                if (res.success) _orders.value = res.data ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to load orders"
            } finally {
                _loading.value = false
            }
        }
    }

    // Admin: fetch all orders
    fun fetchAllOrders() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val res = ApiClient.service.getAllOrders()
                if (res.success) _orders.value = res.data ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Failed to load orders"
            } finally {
                _loading.value = false
            }
        }
    }

    // Place order
    fun placeOrder(userId: Long, customerName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val res = ApiClient.service.placeOrder(userId, OrderRequest(customerName))
                if (res.success && res.data != null) {
                    _placedOrder.value = res.data
                    fetchOrders(userId)
                    onSuccess()
                } else {
                    _error.value = res.message.ifBlank { "Order failed" }
                }
            } catch (e: Exception) {
                _error.value = "Cannot connect to server"
            } finally {
                _loading.value = false
            }
        }
    }

    // Admin: update status
    fun updateStatus(orderId: Long, status: String) {
        viewModelScope.launch {
            try {
                val res = ApiClient.service.updateOrderStatus(orderId, UpdateStatusRequest(status))
                if (res.success && res.data != null) {
                    _orders.value = _orders.value.map {
                        if (it.id == orderId) res.data else it
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to update status"
            }
        }
    }

    fun clearError() { _error.value = null }
}
