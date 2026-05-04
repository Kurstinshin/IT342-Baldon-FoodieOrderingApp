package edu.cit.baldon.foodiemobile.data.local

import android.content.Context
import android.content.SharedPreferences
import edu.cit.baldon.foodiemobile.data.api.ApiClient

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("foodie_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USERNAME = "username"
        private const val KEY_ROLE = "role"
    }

    fun saveSession(token: String, userId: Long, username: String, role: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_USERNAME, username)
            .putString(KEY_ROLE, role)
            .apply()
        ApiClient.setToken(token)
    }

    fun restoreToken() {
        ApiClient.setToken(getToken())
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
    fun getUserId(): Long = prefs.getLong(KEY_USER_ID, 0L)
    fun getUsername(): String = prefs.getString(KEY_USERNAME, "Foodie") ?: "Foodie"
    fun getRole(): String = prefs.getString(KEY_ROLE, "USER") ?: "USER"
    fun isLoggedIn(): Boolean = !getToken().isNullOrBlank()
    fun isAdmin(): Boolean = getRole() == "ADMIN"

    fun clearSession() {
        prefs.edit().clear().apply()
        ApiClient.setToken(null)
    }
}
