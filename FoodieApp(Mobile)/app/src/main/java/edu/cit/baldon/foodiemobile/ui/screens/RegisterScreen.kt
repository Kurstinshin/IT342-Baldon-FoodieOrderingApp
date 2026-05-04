package edu.cit.baldon.foodiemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.baldon.foodiemobile.ui.theme.*
import edu.cit.baldon.foodiemobile.viewmodel.AuthState
import edu.cit.baldon.foodiemobile.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onRegisterSuccess: (role: String) -> Unit,
    onGoLogin: () -> Unit
) {
    var name     by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            onRegisterSuccess((state as AuthState.Success).role)
            vm.reset()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoodieBackground)
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🍔", fontSize = 48.sp)
        Spacer(Modifier.height(6.dp))
        Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = FoodieText)
        Text("Join Foodie today", fontSize = 14.sp, color = FoodieGray)
        Spacer(Modifier.height(28.dp))

        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Full Name") }, singleLine = true,
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        )

        if (state is AuthState.Error) {
            Spacer(Modifier.height(8.dp))
            Text((state as AuthState.Error).message, color = FoodieRed, fontSize = 13.sp)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { vm.register(name, email, password) },
            enabled = state !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FoodieYellow, contentColor = FoodieText)
        ) {
            if (state is AuthState.Loading)
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = FoodieText, strokeWidth = 2.dp)
            else
                Text("Register", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        Spacer(Modifier.height(20.dp))
        TextButton(onClick = onGoLogin) {
            Text("Already have an account? ", color = FoodieGray, fontSize = 13.sp)
            Text("Login", color = FoodiePurple, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}
