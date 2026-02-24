package com.carevalojesus.contactsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.carevalojesus.contactsapp.data.model.Restaurant
import com.carevalojesus.contactsapp.ui.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(viewModel: RestaurantViewModel, navController: NavController) {
    val restaurants by viewModel.restaurants.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    // Variables para saber si estamos editando
    var editingRestaurant by remember { mutableStateOf<Restaurant?>(null) }
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Restaurantes") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingRestaurant = null // Limpiamos porque es uno nuevo
                name = ""
                address = ""
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(restaurants) { restaurant ->
                ListItem(
                    headlineContent = { Text(restaurant.name) },
                    supportingContent = { Text(restaurant.address) },
                    trailingContent = {
                        Row {
                            // BOTÓN DE EDITAR
                            IconButton(onClick = {
                                editingRestaurant = restaurant
                                name = restaurant.name
                                address = restaurant.address
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                            }
                            // BOTÓN DE ELIMINAR
                            IconButton(onClick = { viewModel.deleteRestaurant(restaurant) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    },
                    modifier = Modifier.clickable {
                        navController.navigate("dishes/${restaurant.id}")
                    }
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(if (editingRestaurant == null) "Nuevo Restaurante" else "Editar Restaurante") },
                text = {
                    Column {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dirección") })
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (name.isNotBlank()) {
                            if (editingRestaurant == null) {
                                viewModel.addRestaurant(name, address)
                            } else {
                                // Copiamos el restaurante actual pero con los datos nuevos
                                viewModel.updateRestaurant(editingRestaurant!!.copy(name = name, address = address))
                            }
                            showDialog = false
                        }
                    }) { Text("Guardar") }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
                }
            )
        }
    }
}