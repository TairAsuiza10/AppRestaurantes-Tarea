package com.carevalojesus.contactsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font. FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.carevalojesus.contactsapp.data.model.Restaurant
import com.carevalojesus.contactsapp.ui.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(viewModel: RestaurantViewModel, navController: NavController) {
    val restaurants by viewModel.restaurants.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    var editingRestaurant by remember { mutableStateOf<Restaurant?>(null) }
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Restaurantes", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingRestaurant = null
                    name = ""
                    address = ""
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        if (restaurants.isEmpty()) {
            // Mensaje cuando no hay datos
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No hay restaurantes aún. ¡Agrega uno!", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(restaurants) { restaurant ->
                    // AQUÍ ESTÁ LA MAGIA DEL DISEÑO: Usamos ElevatedCard
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.navigate("dishes/${restaurant.id}") }
                    ) {
                        ListItem(
                            leadingContent = {
                                Icon(Icons.Default.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            headlineContent = {
                                Text(restaurant.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            },
                            supportingContent = {
                                Text(restaurant.address, style = MaterialTheme.typography.bodyMedium)
                            },
                            trailingContent = {
                                Row {
                                    IconButton(onClick = {
                                        editingRestaurant = restaurant
                                        name = restaurant.name
                                        address = restaurant.address
                                        showDialog = true
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.tertiary)
                                    }
                                    IconButton(onClick = { viewModel.deleteRestaurant(restaurant) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            },
                            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                icon = { Icon(Icons.Default.Place, contentDescription = null) },
                title = { Text(if (editingRestaurant == null) "Nuevo Restaurante" else "Editar Restaurante") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Dirección") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (name.isNotBlank()) {
                            if (editingRestaurant == null) {
                                viewModel.addRestaurant(name, address)
                            } else {
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