package edu.cit.baldon.foodiemobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.cit.baldon.foodiemobile.shared.session.SessionManager
import edu.cit.baldon.foodiemobile.feature.*
import edu.cit.baldon.foodiemobile.shared.ui.theme.FoodieTheme

class MainActivity : ComponentActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManager(this)
        session.restoreToken()          // Restore saved JWT to ApiClient

        setContent {
            FoodieTheme {
                FoodieApp(session)
            }
        }
    }
}

@Composable
fun FoodieApp(session: SessionManager) {
    val navController = rememberNavController()

    // Shared ViewModels
    val authVm: AuthViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return AuthViewModel(session) as T
        }
    })
    val dashVm:  DashboardViewModel = viewModel()
    val cartVm:  CartViewModel      = viewModel()
    val orderVm: OrderViewModel     = viewModel()

    // Decide start destination
    val start = when {
        !session.isLoggedIn() -> "login"
        session.isAdmin()     -> "admin"
        else                  -> "dashboard"
    }

    NavHost(navController = navController, startDestination = start) {

        composable("login") {
            LoginScreen(
                vm = authVm,
                onLoginSuccess = { role ->
                    val dest = if (role == "ADMIN") "admin" else "dashboard"
                    navController.navigate(dest) { popUpTo("login") { inclusive = true } }
                },
                onGoRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                vm = authVm,
                onRegisterSuccess = { role ->
                    val dest = if (role == "ADMIN") "admin" else "dashboard"
                    navController.navigate(dest) { popUpTo("register") { inclusive = true } }
                },
                onGoLogin = { navController.popBackStack() }
            )
        }

        composable("dashboard") {
            val userId = session.getUserId()
            DashboardScreen(
                dashboardVm = dashVm,
                cartVm      = cartVm,
                userId      = userId,
                onGoCart    = { navController.navigate("cart") },
                onGoOrders  = { navController.navigate("orders") },
                onLogout    = {
                    session.clearSession()
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable("cart") {
            val userId = session.getUserId()
            CartScreen(
                cartVm     = cartVm,
                userId     = userId,
                onBack     = { navController.popBackStack() },
                onCheckout = { navController.navigate("checkout") }
            )
        }

        composable("checkout") {
            val userId = session.getUserId()
            CheckoutScreen(
                cartVm   = cartVm,
                orderVm  = orderVm,
                userId   = userId,
                onBack   = { navController.popBackStack() },
                onGoOrders = { navController.navigate("orders") { popUpTo("dashboard") } },
                onGoHome = { navController.navigate("dashboard") { popUpTo("dashboard") { inclusive = true } } }
            )
        }

        composable("orders") {
            val userId = session.getUserId()
            OrderHistoryScreen(
                orderVm = orderVm,
                userId  = userId,
                onBack  = { navController.popBackStack() }
            )
        }

        composable("admin") {
            AdminDashboardScreen(
                dashVm   = dashVm,
                orderVm  = orderVm,
                onLogout = {
                    session.clearSession()
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                }
            )
        }
    }
}
