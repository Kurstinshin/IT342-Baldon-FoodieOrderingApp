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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.baldon.foodiemobile.data.model.Food
import edu.cit.baldon.foodiemobile.ui.theme.*
import edu.cit.baldon.foodiemobile.viewmodel.DashboardViewModel
import edu.cit.baldon.foodiemobile.viewmodel.OrderViewModel

@Composable
fun AdminDashboardScreen(
    dashVm: DashboardViewModel,
    orderVm: OrderViewModel,
    onLogout: () -> Unit
) {
    var tab by remember { mutableStateOf(0) }
    val foods  by dashVm.foods.collectAsState()
    val orders by orderVm.orders.collectAsState()

    LaunchedEffect(Unit) { orderVm.fetchAllOrders() }

    Column(Modifier.fillMaxSize().background(FoodieBackground)) {
        // Header
        Row(
            Modifier.fillMaxWidth().background(FoodieSurface).padding(horizontal = 16.dp, vertical = 12.dp),
            Arrangement.SpaceBetween, Alignment.CenterVertically
        ) {
            Text("🍔  Foodie Admin", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = FoodiePurple)
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = FoodieRed),
                border = ButtonDefaults.outlinedButtonBorder
            ) { Text("Logout", fontWeight = FontWeight.Bold) }
        }

        // Tabs
        TabRow(selectedTabIndex = tab, containerColor = FoodieSurface, contentColor = FoodiePurple) {
            Tab(selected = tab == 0, onClick = { tab = 0 }) { Text("🥗 Products", Modifier.padding(12.dp), fontWeight = FontWeight.SemiBold) }
            Tab(selected = tab == 1, onClick = { tab = 1; orderVm.fetchAllOrders() }) { Text("📦 Orders", Modifier.padding(12.dp), fontWeight = FontWeight.SemiBold) }
        }

        when (tab) {
            0 -> ProductsTab(dashVm, foods)
            1 -> OrdersTab(orderVm, orders)
        }
    }
}

@Composable
private fun ProductsTab(dashVm: DashboardViewModel, foods: List<Food>) {
    var showForm  by remember { mutableStateOf(false) }
    var editFood  by remember { mutableStateOf<Food?>(null) }
    var name      by remember { mutableStateOf("") }
    var desc      by remember { mutableStateOf("") }
    var price     by remember { mutableStateOf("") }
    var category  by remember { mutableStateOf("") }
    var img       by remember { mutableStateOf("") }

    fun clearForm() { name=""; desc=""; price=""; category=""; img=""; editFood=null; showForm=false }
    fun loadFood(f: Food) { name=f.name; desc=f.description; price=f.price.toString(); category=f.category; img=f.img; editFood=f; showForm=true }

    Column(Modifier.fillMaxSize()) {
        Button(
            onClick = { if (showForm) clearForm() else { clearForm(); showForm = true } },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FoodieGreen, contentColor = Color.White)
        ) { Text(if (showForm) "✕ Close" else "+ Add Food", fontWeight = FontWeight.Bold) }

        if (showForm) {
            Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (editFood != null) "Edit Food" else "Add New Food", fontWeight = FontWeight.Bold, color = FoodiePurple)
                    listOf("Name" to name, "Description" to desc, "Price" to price, "Category" to category, "Image URL" to img)
                        .forEachIndexed { i, (label, value) ->
                            OutlinedTextField(
                                value = value,
                                onValueChange = { v -> when(i) { 0->name=v; 1->desc=v; 2->price=v; 3->category=v; 4->img=v } },
                                label = { Text(label) }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    Button(
                        onClick = {
                            val p = price.toDoubleOrNull() ?: 0.0
                            if (editFood != null) dashVm.updateFood(editFood!!.id, name, desc, p, category, img) { clearForm() }
                            else dashVm.addFood(name, desc, p, category, img) { clearForm() }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = FoodiePurple, contentColor = Color.White)
                    ) { Text("💾 Save", fontWeight = FontWeight.Bold) }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(foods) { f ->
                Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = FoodieSurface), elevation = CardDefaults.cardElevation(2.dp)) {
                    Row(Modifier.fillMaxWidth().padding(12.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(f.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("P${f.price.toInt()} · ${f.category}", color = FoodieGray, fontSize = 12.sp)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { loadFood(f) }, colors = ButtonDefaults.outlinedButtonColors(contentColor = FoodieOrange)) { Text("✏️") }
                            OutlinedButton(onClick = { dashVm.deleteFood(f.id) }, colors = ButtonDefaults.outlinedButtonColors(contentColor = FoodieRed)) { Text("🗑️") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrdersTab(orderVm: OrderViewModel, orders: List<edu.cit.baldon.foodiemobile.data.model.Order>) {
    val statusOptions = listOf("PENDING", "COMPLETED", "CANCELLED")

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text("All Orders", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            IconButton(onClick = { orderVm.fetchAllOrders() }) { Icon(Icons.Default.Refresh, "Refresh", tint = FoodiePurple) }
        }

        if (orders.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📭", fontSize = 48.sp); Spacer(Modifier.height(12.dp))
                    Text("No orders yet", color = FoodieGray, fontSize = 16.sp)
                }
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(orders) { order ->
                    var expanded by remember { mutableStateOf(false) }
                    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = FoodieSurface), elevation = CardDefaults.cardElevation(2.dp)) {
                        Column(Modifier.fillMaxWidth().padding(14.dp)) {
                            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                                Text("Order #${order.id}", fontWeight = FontWeight.Bold)
                                StatusBadge(order.status)
                            }
                            Text("👤 ${order.customerName} · P${order.totalAmount.toInt()}", color = FoodieGray, fontSize = 12.sp)
                            order.items.forEach { i -> Text("• ${i.quantity}x ${i.food.name}", fontSize = 12.sp, color = FoodieText) }
                            Spacer(Modifier.height(10.dp))
                            // Status dropdown
                            Box {
                                OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                                    Text("Status: ${order.status}", fontWeight = FontWeight.Bold)
                                }
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    statusOptions.forEach { s ->
                                        DropdownMenuItem(text = { Text(s) }, onClick = {
                                            orderVm.updateStatus(order.id, s)
                                            expanded = false
                                        })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
