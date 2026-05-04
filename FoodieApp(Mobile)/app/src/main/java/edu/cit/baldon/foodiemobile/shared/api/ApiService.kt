package edu.cit.baldon.foodiemobile.shared.api

import edu.cit.baldon.foodiemobile.shared.model.*
import retrofit2.http.*

interface ApiService {

    // ── Auth ─────────────────────────────────────────────────────────────────
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<AuthData>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<AuthData>

    // ── Foods ─────────────────────────────────────────────────────────────────
    @GET("foods")
    suspend fun getFoods(): ApiResponse<List<Food>>

    @POST("foods")
    suspend fun addFood(@Body food: FoodRequest): ApiResponse<Food>

    @PUT("foods/{id}")
    suspend fun updateFood(@Path("id") id: Long, @Body food: FoodRequest): ApiResponse<Food>

    @DELETE("foods/{id}")
    suspend fun deleteFood(@Path("id") id: Long): ApiResponse<Void>

    // ── Cart ──────────────────────────────────────────────────────────────────
    @GET("cart/{userId}")
    suspend fun getCart(@Path("userId") userId: Long): ApiResponse<List<CartItem>>

    @POST("cart/{userId}")
    suspend fun addToCart(
        @Path("userId") userId: Long,
        @Body request: AddToCartRequest
    ): ApiResponse<CartItem>

    @PUT("cart/{userId}/{itemId}")
    suspend fun updateCartItem(
        @Path("userId") userId: Long,
        @Path("itemId") itemId: Long,
        @Body request: UpdateCartRequest
    ): ApiResponse<CartItem>

    @DELETE("cart/{userId}/{itemId}")
    suspend fun removeCartItem(
        @Path("userId") userId: Long,
        @Path("itemId") itemId: Long
    ): ApiResponse<Void>

    // ── Orders ────────────────────────────────────────────────────────────────
    @POST("orders/{userId}")
    suspend fun placeOrder(
        @Path("userId") userId: Long,
        @Body request: OrderRequest
    ): ApiResponse<Order>

    @GET("orders/{userId}")
    suspend fun getOrdersByUser(@Path("userId") userId: Long): ApiResponse<List<Order>>

    @GET("orders")
    suspend fun getAllOrders(): ApiResponse<List<Order>>

    @PATCH("orders/{orderId}/status")
    suspend fun updateOrderStatus(
        @Path("orderId") orderId: Long,
        @Body request: UpdateStatusRequest
    ): ApiResponse<Order>
}
