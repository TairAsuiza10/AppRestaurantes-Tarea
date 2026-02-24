package com.carevalojesus.contactsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.carevalojesus.contactsapp.ui.navigation.AppNavigation
import com.carevalojesus.contactsapp.ui.theme.ContactsAppTheme
import com.carevalojesus.contactsapp.ui.viewmodel.RestaurantViewModel

class MainActivity : ComponentActivity() {

    // Aquí inicializamos nuestro nuevo ViewModel
    private val viewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactsAppTheme {
                // Le pasamos el viewModel a nuestra navegación
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}