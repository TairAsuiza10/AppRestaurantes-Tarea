package com.carevalojesus.contactsapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.carevalojesus.contactsapp.data.model.Dish
import com.carevalojesus.contactsapp.ui.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContactScreen(viewModel: RestaurantViewModel, restaurantId: Int, navController: NavController) {
    LaunchedEffect(restaurantId) {
        viewModel.loadDishesForRestaurant(restaurantId)
    }

    val dishes by viewModel.dishes.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    // Variables para saber si estamos editando
    var editingDish by remember { mutableStateOf<Dish?>(null) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menú del Restaurante") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingDish = null
                name = ""
                price = ""
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Plato")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(dishes) { dish ->
                ListItem(
                    headlineContent = { Text(dish.name) },
                    supportingContent = { Text("S/ ${dish.price}") },
                    trailingContent = {
                        Row {
                            // BOTÓN DE EDITAR
                            IconButton(onClick = {
                                editingDish = dish
                                name = dish.name
                                price = dish.price.toString()
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                            }
                            // BOTÓN DE ELIMINAR
                            IconButton(onClick = { viewModel.deleteDish(dish) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(if (editingDish == null) "Nuevo Plato" else "Editar Plato") },
                text = {
                    Column {
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre del plato") })
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Precio (Ej: 15.50)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (name.isNotBlank()) {
                            val priceDouble = price.toDoubleOrNull() ?: 0.0
                            if (editingDish == null) {
                                viewModel.addDish(restaurantId, name, priceDouble)
                            } else {
                                // Actualizamos el plato existente
                                viewModel.updateDish(editingDish!!.copy(name = name, price = priceDouble))
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