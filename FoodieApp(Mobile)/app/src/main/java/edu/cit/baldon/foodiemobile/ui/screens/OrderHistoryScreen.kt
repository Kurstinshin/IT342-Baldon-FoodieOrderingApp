package edu.cit.baldon.foodiemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.baldon.foodiemobile.ui.theme.*
import edu.cit.baldon.foodiemobile.viewmodel.OrderViewModel

@Composable
fun OrderHistoryScreen(orderVm: OrderViewModel, userId: Long, onBack: () -> Unit) {
    val orders by orderVm.orders.collectAsState()
    val loading by orderVm.loading.collectAsState()

    LaunchedEffect(userId) { orderVm.fetchOrders(userId) }

    Column(modifier = Modifier.fillMaxSize().background(FoodieBackground)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(FoodieSurface).padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back", tint = FoodieText) }
            Text("Order History", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f))
            IconButton(onClick = { orderVm.fetchOrders(userId) }) {
                Icon(Icons.Default.Refresh, "Refresh", tint = FoodieText)
            }
        }

        when {
            loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = FoodiePurple)
            }
            orders.isEmpty() -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🍽️", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("No orders yet!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Start ordering some food", color = FoodieGray, fontSize = 14.sp)
                }
            }
            else -> LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(orders) { order ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = FoodieSurface),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(14.dp)) {
                            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                                Text("Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                StatusBadge(order.status)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("👤 ${order.customerName}", color = FoodieGray, fontSize = 12.sp)
                            Spacer(Modifier.height(8.dp))
                            Column(
                                Modifier.fillMaxWidth().background(FoodieBackground, RoundedCornerShape(8.dp)).padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                order.items.forEach { item ->
                                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                        Text("${item.quantity}x ${item.food.name}", fontSize = 12.sp)
                                        Text("P${(item.food.price * item.quantity).toInt()}", fontSize = 12.sp, color = FoodieGray)
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("Total: P${order.totalAmount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.align(Alignment.End))
                        }
                    }
                }
            }
        }
    }
}
