package edu.cit.baldon.foodiemobile.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import edu.cit.baldon.foodiemobile.shared.model.Food
import edu.cit.baldon.foodiemobile.shared.ui.theme.*
import edu.cit.baldon.foodiemobile.feature.CartViewModel
import edu.cit.baldon.foodiemobile.feature.DashboardViewModel

@Composable
fun DashboardScreen(
    dashboardVm: DashboardViewModel,
    cartVm: CartViewModel,
    userId: Long,
    onGoCart: () -> Unit,
    onGoOrders: () -> Unit,
    onLogout: () -> Unit
) {
    val foods by dashboardVm.foods.collectAsState()
    val cart  by cartVm.cart.collectAsState()
    var search by remember { mutableStateOf("") }
    var menuExpanded by remember { mutableStateOf(false) }
    val liked = remember { mutableStateMapOf<Long, Boolean>() }

    val filtered = foods.filter { it.name.contains(search, ignoreCase = true) }
    val cartCount = cart.sumOf { it.quantity }

    LaunchedEffect(userId) { cartVm.fetchCart(userId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoodieBackground)
    ) {
        // ── Top Bar ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(FoodieBackground)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Three-dot menu (left)
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = FoodieText)
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(text = { Text("My Orders") }, onClick = { menuExpanded = false; onGoOrders() })
                    DropdownMenuItem(text = { Text("Logout", color = FoodieRed) }, onClick = { menuExpanded = false; onLogout() })
                }
            }

            // Title (center)
            Text(
                "Popular Food",
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = FoodieText
            )

            // Cart icon (right)
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                IconButton(onClick = onGoCart) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = FoodieText)
                }
                if (cartCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 4.dp, y = (-4).dp)
                            .size(16.dp)
                            .background(FoodieRed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(cartCount.toString(), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ── Search Bar ───────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .background(FoodieYellow, RoundedCornerShape(50.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                decorationBox = { inner ->
                    if (search.isEmpty()) Text("Search food...", color = Color(0xFF8B6914), fontSize = 14.sp)
                    inner()
                }
            )
            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF555555), modifier = Modifier.size(18.dp))
        }

        Spacer(Modifier.height(12.dp))

        // ── Food Grid ────────────────────────────────────────────────────────
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered) { food ->
                FoodCard(
                    food = food,
                    isLiked = liked[food.id] == true,
                    onLike = { liked[food.id] = !(liked[food.id] ?: false) },
                    onAdd  = { cartVm.addToCart(userId, food) }
                )
            }
        }
    }
}

@Composable
fun FoodCard(food: Food, isLiked: Boolean, onLike: () -> Unit, onAdd: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FoodieSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Image + Heart
            Box {
                AsyncImage(
                    model = food.img,
                    contentDescription = food.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                IconButton(onClick = onLike, modifier = Modifier.align(Alignment.TopEnd).size(32.dp)) {
                    Icon(
                        if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) FoodieRed else Color(0xFFBBBBBB),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(food.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = FoodieText, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (food.description.isNotBlank()) {
                Text(food.description, fontSize = 11.sp, color = FoodieGray, maxLines = 3, overflow = TextOverflow.Ellipsis, lineHeight = 15.sp)
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("P${food.price.toInt()}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = FoodieText)
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(FoodieYellow, CircleShape)
                        .clickable { onAdd() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

// Needed for BasicTextField with placeholder
@Composable
private fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit = { it() }
) = androidx.compose.foundation.text.BasicTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    singleLine = singleLine,
    decorationBox = decorationBox,
    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp, color = FoodieText)
)
