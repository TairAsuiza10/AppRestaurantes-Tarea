package com.carevalojesus.contactsapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.carevalojesus.contactsapp.data.database.ContactDatabase
import com.carevalojesus.contactsapp.data.model.Dish
import com.carevalojesus.contactsapp.data.model.Restaurant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RestaurantViewModel(application: Application) : AndroidViewModel(application) {

    // Conectamos con nuestro nuevo DAO
    private val dao = ContactDatabase.getDatabase(application).restaurantDao()

    // 1. Estado para la lista de Restaurantes
    val restaurants: StateFlow<List<Restaurant>> = dao.getAllRestaurants()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // 2. Estado para los Platos (cambia según el restaurante que seleccionemos)
    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    // ========================
    // FUNCIONES DE RESTAURANTE
    // ========================
    fun addRestaurant(name: String, address: String) = viewModelScope.launch {
        dao.insertRestaurant(Restaurant(name = name, address = address))
    }

    fun deleteRestaurant(restaurant: Restaurant) = viewModelScope.launch {
        dao.deleteRestaurant(restaurant)
    }

    // ========================
    // FUNCIONES DE PLATOS
    // ========================

    // Carga los platos de un restaurante específico
    fun loadDishesForRestaurant(restaurantId: Int) {
        viewModelScope.launch {
            dao.getDishesForRestaurant(restaurantId).collect { listaPlatos ->
                _dishes.value = listaPlatos
            }
        }
    }

    fun addDish(restaurantId: Int, name: String, price: Double) = viewModelScope.launch {
        dao.insertDish(Dish(restaurantId = restaurantId, name = name, price = price))
    }

    fun deleteDish(dish: Dish) = viewModelScope.launch {
        dao.deleteDish(dish)
    }
}