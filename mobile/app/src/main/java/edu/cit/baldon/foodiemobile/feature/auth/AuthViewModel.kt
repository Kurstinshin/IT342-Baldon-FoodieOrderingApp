package edu.cit.baldon.foodiemobile.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cit.baldon.foodiemobile.shared.api.ApiClient
import edu.cit.baldon.foodiemobile.shared.session.SessionManager
import edu.cit.baldon.foodiemobile.shared.model.LoginRequest
import edu.cit.baldon.foodiemobile.shared.model.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val role: String) : AuthState()
    object Registered : AuthState()                        // <-- new state
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val session: SessionManager) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state = _state.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = AuthState.Error("Email and password required")
            return
        }
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val res = ApiClient.service.login(LoginRequest(email.trim(), password))
                if (res.success && res.data != null) {
                    val d = res.data
                    session.saveSession(d.token, d.userId, d.username, d.role)
                    _state.value = AuthState.Success(d.role)
                } else {
                    _state.value = AuthState.Error(res.message.ifBlank { "Login failed" })
                }
            } catch (e: Exception) {
                _state.value = AuthState.Error("Cannot connect to server")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _state.value = AuthState.Error("All fields are required")
            return
        }
        viewModelScope.launch {
            _state.value = AuthState.Loading
            try {
                val res = ApiClient.service.register(RegisterRequest(name.trim(), email.trim(), password))
                if (res.success && res.data != null) {
                    // Don't save session — redirect to login instead
                    _state.value = AuthState.Registered
                } else {
                    _state.value = AuthState.Error(res.message.ifBlank { "Registration failed" })
                }
            } catch (e: Exception) {
                _state.value = AuthState.Error("Cannot connect to server")
            }
        }
    }

    fun reset() { _state.value = AuthState.Idle }
}
