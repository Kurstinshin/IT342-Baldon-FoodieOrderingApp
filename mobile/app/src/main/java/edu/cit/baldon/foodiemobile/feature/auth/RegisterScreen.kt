package edu.cit.baldon.foodiemobile.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// Brand colors
private val OrangeGrad1 = Color(0xFFE8913A)
private val OrangeGrad2 = Color(0xFFFBD65B)
private val DarkText    = Color(0xFF333333)
private val GrayText    = Color(0xFF999999)
private val OrangeText  = Color(0xFFE8913A)

@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onRegisterSuccess: (role: String) -> Unit,
    onGoLogin: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state is AuthState.Registered) {
            onGoLogin()
            vm.reset()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ── FOOD IMAGE AT TOP ──────────────────────────────────────────────────
        AsyncImage(
            model = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600&q=80",
            contentDescription = "Salad",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        )

        // ── CARD BODY ──────────────────────────────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App name
                Text("🍽️ FoodieOrderingApp", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkText)

                Spacer(Modifier.height(20.dp))

                // ── TABS: Sign In / Sign Up ────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Sign In tab — inactive
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .clickable { onGoLogin() }
                    ) {
                        Text("Sign In", fontWeight = FontWeight.Normal, fontSize = 15.sp, color = GrayText)
                        Spacer(Modifier.height(4.dp))
                        Box(Modifier.width(50.dp).height(2.dp)) // invisible underline
                    }
                    // Sign Up tab — active
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text("Sign Up", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OrangeText)
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(2.dp)
                                .background(OrangeText)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // ── FIRST NAME ─────────────────────────────────────────────────
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = { Text("First Name", color = GrayText, fontSize = 14.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = OrangeText
                    )
                )

                Spacer(Modifier.height(12.dp))

                // ── LAST NAME ──────────────────────────────────────────────────
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = { Text("Last Name", color = GrayText, fontSize = 14.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = OrangeText
                    )
                )

                Spacer(Modifier.height(12.dp))

                // ── EMAIL ──────────────────────────────────────────────────────
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email address", color = GrayText, fontSize = 14.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = OrangeText
                    )
                )

                Spacer(Modifier.height(12.dp))

                // ── PASSWORD ───────────────────────────────────────────────────
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password", color = GrayText, fontSize = 14.sp) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = OrangeText
                    )
                )

                // Error message
                if (state is AuthState.Error) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        (state as AuthState.Error).message,
                        color = Color(0xFFE74C3C),
                        fontSize = 12.sp
                    )
                }

                Spacer(Modifier.height(20.dp))

                // ── GRADIENT CREATE ACCOUNT BUTTON ─────────────────────────────
                Button(
                    onClick = {
                        val fullName = "$firstName $lastName".trim()
                        vm.register(fullName, email, password)
                    },
                    enabled = state !is AuthState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(listOf(OrangeGrad1, OrangeGrad2)),
                                RoundedCornerShape(25.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (state is AuthState.Loading)
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        else
                            Text("Create Account", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ── OR DIVIDER ─────────────────────────────────────────────────
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                    Text("  OR  ", color = GrayText, fontSize = 13.sp)
                    HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE0E0E0))
                }

                Spacer(Modifier.height(16.dp))

                // ── SOCIAL ICONS ───────────────────────────────────────────────
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    AsyncImage(
                        model = "https://cdn-icons-png.flaticon.com/512/2991/2991148.png",
                        contentDescription = "Google",
                        modifier = Modifier.size(32.dp)
                    )
                    AsyncImage(
                        model = "https://cdn-icons-png.flaticon.com/512/124/124010.png",
                        contentDescription = "Facebook",
                        modifier = Modifier.size(32.dp)
                    )
                    AsyncImage(
                        model = "https://cdn-icons-png.flaticon.com/512/733/733579.png",
                        contentDescription = "Twitter",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}
