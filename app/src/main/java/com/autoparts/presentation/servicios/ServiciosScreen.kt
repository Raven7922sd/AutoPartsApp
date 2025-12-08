package com.autoparts.presentation.servicios

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autoparts.ui.theme.AutoPartsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiciosScreen(
    navController: NavController,
    viewModel: ServiciosViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = { navController.navigate("home_screen") {
        popUpTo("home_screen") { inclusive = true }
        launchSingleTop = true
    }},
    onNavigateToCategorias: () -> Unit = { navController.navigate(com.autoparts.presentation.navigation.Screen.Categorias.route) },
    onNavigateToCarrito: () -> Unit = { navController.navigate(com.autoparts.presentation.navigation.Screen.Carrito.route) }
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ServiciosUiEffect.NavigateToServicioDetalle -> {
                    navController.navigate(
                        com.autoparts.presentation.navigation.Screen.ServicioDetalle.createRoute(effect.servicioId)
                    )
                }
                is ServiciosUiEffect.NavigateToCrearCita -> {
                }
            }
        }
    }

    ServiciosScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        searchQuery = searchQuery,
        onSearchQueryChange = { },
        isSearching = isSearching,
        onSearchToggle = { !isSearching },
        onBackClick = { navController.popBackStack() },
        onNavigateToHome = onNavigateToHome,
        onNavigateToCategorias = onNavigateToCategorias,
        onNavigateToCarrito = onNavigateToCarrito
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiciosScreenContent(
    uiState: ServiciosUiState,
    onEvent: (ServiciosUiEvent) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearching: Boolean,
    onSearchToggle: () -> Unit,
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToCategorias: () -> Unit = {},
    onNavigateToCarrito: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Servicios Automotrices") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onSearchToggle) {
                        Icon(Icons.Default.Search, "Buscar")
                    }
                }
            )
        },
        bottomBar = {
            com.autoparts.presentation.inicio.BottomNavigationBar(
                selectedItem = 2,
                onItemSelected = { index ->
                    when (index) {
                        0 -> onNavigateToHome()
                        1 -> onNavigateToCategorias()
                        2 -> { }
                        3 -> onNavigateToCarrito()
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isSearching) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        onSearchQueryChange(it)
                        onEvent(ServiciosUiEvent.OnSearchQueryChanged(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Buscar servicio...") },
                    singleLine = true
                )
            }

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.serviciosFiltrados) { servicio ->
                            ServicioCard(
                                servicio = servicio,
                                onClick = {
                                    onEvent(ServiciosUiEvent.OnServicioClick(servicio.servicioId))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServicioCard(
    servicio: ServicioUI,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            servicio.imagen?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = servicio.nombre,
                    modifier = Modifier
                        .size(80.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = servicio.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = servicio.precioFormateado,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Duración: ${servicio.duracionFormateada}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServiciosScreenPreview() {
    AutoPartsAppTheme {
        ServiciosScreenContent(
            uiState = ServiciosUiState(
                serviciosFiltrados = listOf(
                    ServicioUI(
                        servicioId = 1,
                        nombre = "Cambio de Aceite",
                        precioFormateado = "RD$ 1,500.00",
                        descripcion = "Cambio completo de aceite del motor",
                        duracionFormateada = "1 hora",
                        imagen = null,
                        solicitados = 15,
                        fechaServicio = "2024-01-15"
                    )
                )
            ),
            onEvent = {},
            searchQuery = "",
            onSearchQueryChange = {},
            isSearching = false,
            onSearchToggle = {},
            onBackClick = {}
        )
    }
}