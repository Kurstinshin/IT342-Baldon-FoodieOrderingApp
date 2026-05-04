package edu.cit.baldon.foodiemobile.data.model

// ── API Wrapper ───────────────────────────────────────────────────────────────
data class ApiResponse<T>(
    val success: Boolean = false,
    val message: String = "",
    val data: T? = null,
    val timestamp: String? = null
)

// ── Auth ──────────────────────────────────────────────────────────────────────
data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val name: String, val email: String, val password: String)
data class AuthData(
    val token: String = "",
    val userId: Long = 0,
    val username: String = "",
    val role: String = ""
)

// ── Food ──────────────────────────────────────────────────────────────────────
data class Food(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val img: String = ""
)

data class FoodRequest(
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val img: String
)

// ── Cart ──────────────────────────────────────────────────────────────────────
data class CartItem(
    val id: Long = 0,
    val food: Food = Food(),
    val quantity: Int = 0
)

data class AddToCartRequest(val foodId: Long, val quantity: Int)
data class UpdateCartRequest(val quantity: Int)

// ── Orders ────────────────────────────────────────────────────────────────────
data class OrderItem(
    val id: Long = 0,
    val food: Food = Food(),
    val quantity: Int = 0
)

data class Order(
    val id: Long = 0,
    val customerName: String = "",
    val totalAmount: Double = 0.0,
    val status: String = "PENDING",
    val items: List<OrderItem> = emptyList()
)

data class OrderRequest(val customerName: String)
data class UpdateStatusRequest(val status: String)
