package edu.cit.baldon.foodiemobile.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import edu.cit.baldon.foodiemobile.shared.ui.theme.*
import edu.cit.baldon.foodiemobile.feature.CartViewModel

@Composable
fun CartScreen(
    cartVm: CartViewModel,
    userId: Long,
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val cart by cartVm.cart.collectAsState()
    val total = cart.sumOf { it.food.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoodieBackground)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(FoodieSurface)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = FoodieText)
            }
            Text("My Order", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = FoodieText)
        }

        if (cart.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🍽️", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("Your cart is empty", color = FoodieGray, fontSize = 16.sp)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = FoodieYellow, contentColor = FoodieText)
                    ) { Text("Browse Food", fontWeight = FontWeight.Bold) }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cart) { item ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = FoodieSurface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = item.food.img,
                                contentDescription = item.food.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(item.food.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("P${item.food.price.toInt()}", color = FoodieGray, fontSize = 13.sp)
                            }
                            // Qty controls
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(
                                    modifier = Modifier.size(26.dp).background(Color(0xFFEEDCCA), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TextButton(onClick = { cartVm.decreaseQty(userId, item) }, contentPadding = PaddingValues(0.dp), modifier = Modifier.size(26.dp)) {
                                        Text("−", fontWeight = FontWeight.Bold, color = FoodieText, fontSize = 16.sp)
                                    }
                                }
                                Text(item.quantity.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Box(
                                    modifier = Modifier.size(26.dp).background(FoodieYellow, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TextButton(onClick = { cartVm.increaseQty(userId, item) }, contentPadding = PaddingValues(0.dp), modifier = Modifier.size(26.dp)) {
                                        Text("+", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                                    }
                                }
                            }
                            Spacer(Modifier.width(12.dp))
                            Text("P${(item.food.price * item.quantity).toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            // Bottom total + checkout
            Surface(shadowElevation = 8.dp, color = FoodieSurface) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text("P${total.toInt()}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onCheckout,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF47A67F), contentColor = Color.White)
                    ) { Text("Checkout", fontWeight = FontWeight.Bold, fontSize = 15.sp) }
                }
            }
        }
    }
}
