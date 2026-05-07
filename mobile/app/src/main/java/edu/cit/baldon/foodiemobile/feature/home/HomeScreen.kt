package edu.cit.baldon.foodiemobile.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val GoldYellow  = Color(0xFFFBD65B)
private val DarkText    = Color(0xFF333333)
private val GrayText    = Color(0xFF666666)
private val OrangeGrad1 = Color(0xFFE8913A)

@Composable
fun HomeScreen(
    onGoLogin: () -> Unit,
    onGoRegister: () -> Unit,
    onOrderFood: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        // ── TOP BAR ────────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo — italic gold
            Text(
                text = "Foodie\nOrdering\nApp",
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                lineHeight = 19.sp,
                color = GoldYellow
            )

            Spacer(Modifier.weight(1f))

            // Icons + Login
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Search, "Search", tint = DarkText, modifier = Modifier.size(22.dp))
                Icon(Icons.Outlined.ShoppingCart, "Cart", tint = DarkText, modifier = Modifier.size(22.dp))
                Icon(Icons.Outlined.Notifications, "Alerts", tint = DarkText, modifier = Modifier.size(22.dp))

                OutlinedButton(
                    onClick = onGoLogin,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldYellow),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Login", fontSize = 13.sp, color = DarkText)
                }
            }
        }

        // ── NAV LINKS (centered) ───────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            listOf("Home", "About", "Blog", "Service", "Contact us").forEachIndexed { i, label ->
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = if (label == "Home") FontWeight.Bold else FontWeight.Normal,
                    color = if (label == "Home") DarkText else GrayText
                )
                if (i < 4) Spacer(Modifier.width(20.dp))
            }
        }

        Spacer(Modifier.height(32.dp))

        // ── CIRCULAR FOOD IMAGE ────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentAlignment = Alignment.Center
        ) {
            // Decorative circles
            Box(Modifier.size(220.dp).border(1.5.dp, GoldYellow.copy(alpha = 0.5f), CircleShape))
            Box(Modifier.size(250.dp).border(1.dp, OrangeGrad1.copy(alpha = 0.3f), CircleShape))

            // Main food image
            AsyncImage(
                model = "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=400&q=80",
                contentDescription = "Food",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(190.dp).clip(CircleShape)
            )

            // Small decorative food images
            AsyncImage(
                model = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=100&q=60",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(48.dp).clip(CircleShape)
                    .align(Alignment.TopStart).offset(x = 50.dp, y = 20.dp)
            )
            AsyncImage(
                model = "https://images.unsplash.com/photo-1529692236671-f1f6cf9683ba?w=100&q=60",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(42.dp).clip(CircleShape)
                    .align(Alignment.BottomStart).offset(x = 60.dp, y = (-15).dp)
            )
            AsyncImage(
                model = "https://images.unsplash.com/photo-1553530666-ba11a90a3546?w=100&q=60",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(44.dp).clip(CircleShape)
                    .align(Alignment.CenterEnd).offset(x = (-40).dp, y = (-30).dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        // ── DESCRIPTION TEXT (centered) ────────────────────────────────────────
        Text(
            text = "Exploring new food with different kinds that you can try at this place and get a good price from us as well we will make a good impact to our customers",
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = GrayText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(Modifier.height(28.dp))

        // ── BUTTONS (centered) ─────────────────────────────────────────────────
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Learn More — outlined yellow
            OutlinedButton(
                onClick = { },
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldYellow),
                modifier = Modifier.height(44.dp)
            ) {
                Text("Learn More", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
            }

            // Order Food — yellow with cart icon
            Button(
                onClick = onOrderFood,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldYellow,
                    contentColor = DarkText
                ),
                modifier = Modifier.height(44.dp)
            ) {
                Text("Order Food", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(6.dp))
                Icon(Icons.Outlined.ShoppingCart, null, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(Modifier.height(48.dp))

        // ── HOW YOU CAN ORDER (centered) ───────────────────────────────────────
        Text(
            text = "How You Can Order",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = DarkText
        )

        Spacer(Modifier.height(40.dp))
    }
}
