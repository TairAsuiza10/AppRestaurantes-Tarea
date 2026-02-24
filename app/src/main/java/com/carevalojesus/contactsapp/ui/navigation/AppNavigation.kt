package com.carevalojesus.contactsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carevalojesus.contactsapp.ui.screens.AddEditContactScreen
import com.carevalojesus.contactsapp.ui.screens.ContactListScreen
import com.carevalojesus.contactsapp.ui.viewmodel.RestaurantViewModel

@Composable
fun AppNavigation(viewModel: RestaurantViewModel) {
    val navController = rememberNavController()

    // Nuestro punto de inicio son los Restaurantes
    NavHost(navController = navController, startDestination = "restaurants") {
        composable("restaurants") {
            ContactListScreen(viewModel = viewModel, navController = navController)
        }
        // Al tocar un restaurante, navegamos a sus platos usando su ID
        composable("dishes/{restaurantId}") { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId")?.toIntOrNull() ?: 0
            AddEditContactScreen(viewModel = viewModel, restaurantId = restaurantId, navController = navController)
        }
    }
}