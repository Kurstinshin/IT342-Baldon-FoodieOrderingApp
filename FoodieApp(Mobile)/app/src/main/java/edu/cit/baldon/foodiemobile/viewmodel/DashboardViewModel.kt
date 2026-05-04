package edu.cit.baldon.foodiemobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cit.baldon.foodiemobile.data.api.ApiClient
import edu.cit.baldon.foodiemobile.data.model.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods = _foods.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init { fetchFoods() }

    fun fetchFoods() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val res = ApiClient.service.getFoods()
                if (res.success) _foods.value = res.data ?: emptyList()
                else _error.value = res.message
            } catch (e: Exception) {
                _error.value = "Failed to load food items"
            } finally {
                _loading.value = false
            }
        }
    }

    // Admin: add food
    fun addFood(name: String, desc: String, price: Double, cat: String, img: String, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                ApiClient.service.addFood(
                    edu.cit.baldon.foodiemobile.data.model.FoodRequest(name, desc, price, cat, img)
                )
                fetchFoods()
                onDone()
            } catch (e: Exception) { _error.value = "Failed to add food" }
        }
    }

    // Admin: update food
    fun updateFood(id: Long, name: String, desc: String, price: Double, cat: String, img: String, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                ApiClient.service.updateFood(
                    id, edu.cit.baldon.foodiemobile.data.model.FoodRequest(name, desc, price, cat, img)
                )
                fetchFoods()
                onDone()
            } catch (e: Exception) { _error.value = "Failed to update food" }
        }
    }

    // Admin: delete food
    fun deleteFood(id: Long) {
        viewModelScope.launch {
            try {
                ApiClient.service.deleteFood(id)
                fetchFoods()
            } catch (e: Exception) { _error.value = "Failed to delete food" }
        }
    }
}
