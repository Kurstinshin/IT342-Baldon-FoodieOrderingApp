package edu.cit.baldon.foodiemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.baldon.foodiemobile.ui.theme.*
import edu.cit.baldon.foodiemobile.viewmodel.CartViewModel
import edu.cit.baldon.foodiemobile.viewmodel.OrderViewModel

@Composable
fun CheckoutScreen(
    cartVm: CartViewModel,
    orderVm: OrderViewModel,
    userId: Long,
    onBack: () -> Unit,
    onGoOrders: () -> Unit,
    onGoHome: () -> Unit
) {
    val cart     by cartVm.cart.collectAsState()
    val orders   by orderVm.orders.collectAsState()
    val placed   by orderVm.placedOrder.collectAsState()
    val loading  by orderVm.loading.collectAsState()
    val total    = cart.sumOf { it.food.price * it.quantity }

    var name    by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var card    by remember { mutableStateOf("") }
    var orderDone by remember { mutableStateOf(false) }

    LaunchedEffect(placed) {
        if (placed != null) {
            orderVm.fetchOrders(userId)
            orderDone = true
        }
    }

    if (orderDone) {
        // ── Success Screen ────────────────────────────────────────────────────
        Column(
            modifier = Modifier.fillMaxSize().background(FoodieBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            Text("🎉", fontSize = 64.sp)
            Spacer(Modifier.height(12.dp))
            Text("Order Placed!", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = FoodieText)
            placed?.let { o ->
                Text("Order #${o.id}", color = FoodieGray, fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                StatusBadge(o.status)
            }
            Spacer(Modifier.height(24.dp))

            // Order History List
            Text("Your Order History", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = FoodieText, modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth())
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(orders) { order ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = FoodieSurface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(14.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                StatusBadge(order.status)
                            }
                            Spacer(Modifier.height(6.dp))
                            order.items.forEach { item ->
                                Text("• ${item.quantity}x ${item.food.name}", fontSize = 12.sp, color = FoodieGray)
                            }
                            Spacer(Modifier.height(6.dp))
                            Text("Total: P${order.totalAmount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.align(Alignment.End))
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onGoHome, modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FoodieYellow, contentColor = FoodieText)
                ) { Text("Order More", fontWeight = FontWeight.Bold) }
                Button(
                    onClick = onGoOrders, modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FoodiePurple, contentColor = Color.White)
                ) { Text("All Orders", fontWeight = FontWeight.Bold) }
            }
        }
    } else {
        // ── Payment Form ──────────────────────────────────────────────────────
        Column(modifier = Modifier.fillMaxSize().background(FoodieBackground)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().background(FoodieSurface).padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back", tint = FoodieText) }
                Text("Payment", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = FoodieText)
            }

            // Progress steps
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier.size(24.dp).background(FoodieYellow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp)) }
                    if (it < 2) Spacer(Modifier.width(20.dp).height(2.dp).background(FoodiePurple))
                }
            }

            Column(modifier = Modifier.weight(1f).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Delivery Address") },
                    singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Text("📍") })
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Cardholder Name") },
                    singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = card, onValueChange = { card = it }, label = { Text("Card Number") },
                    singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("XXXX XXXX XXXX XXXX") })

                Divider(color = Color(0xFFE0C8B0))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Amount Payable", fontSize = 15.sp, color = FoodieText)
                    Text("P${total.toInt()}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = FoodieYellowDark)
                }
            }

            Button(
                onClick = {
                    if (name.isNotBlank() && address.isNotBlank() && card.isNotBlank()) {
                        orderVm.placeOrder(userId, name) {}
                    }
                },
                enabled = !loading && name.isNotBlank() && address.isNotBlank() && card.isNotBlank(),
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FoodieYellow, contentColor = FoodieText)
            ) {
                if (loading) CircularProgressIndicator(Modifier.size(20.dp), color = FoodieText, strokeWidth = 2.dp)
                else Text("Pay Now", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bg, fg) = when (status) {
        "COMPLETED" -> Color(0xFFD4EDDA) to Color(0xFF27AE60)
        "CANCELLED" -> Color(0xFFFDE8E8) to Color(0xFFE74C3C)
        else        -> Color(0xFFFFF3CD) to Color(0xFFF39C12)
    }
    Surface(shape = RoundedCornerShape(20.dp), color = bg) {
        Text(status, color = fg, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}
