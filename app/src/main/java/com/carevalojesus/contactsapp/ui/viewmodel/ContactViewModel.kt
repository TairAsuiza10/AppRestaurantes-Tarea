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

    private val dao = ContactDatabase.getDatabase(application).restaurantDao()

    val restaurants: StateFlow<List<Restaurant>> = dao.getAllRestaurants()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    // ========================
    // FUNCIONES DE RESTAURANTE
    // ========================
    fun addRestaurant(name: String, address: String) = viewModelScope.launch {
        dao.insertRestaurant(Restaurant(name = name, address = address))
    }

    // NUEVA FUNCIÓN: Actualizar restaurante
    fun updateRestaurant(restaurant: Restaurant) = viewModelScope.launch {
        dao.updateRestaurant(restaurant)
    }

    fun deleteRestaurant(restaurant: Restaurant) = viewModelScope.launch {
        dao.deleteRestaurant(restaurant)
    }

    // ========================
    // FUNCIONES DE PLATOS
    // ========================
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

    // NUEVA FUNCIÓN: Actualizar plato
    fun updateDish(dish: Dish) = viewModelScope.launch {
        dao.updateDish(dish)
    }

    fun deleteDish(dish: Dish) = viewModelScope.launch {
        dao.deleteDish(dish)
    }
}