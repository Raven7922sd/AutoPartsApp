package com.autoparts.presentation.productodetalle

import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.autoparts.presentation.components.ProductImage
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetalleScreen(
    navController: NavController,
    productoId: Int,
    viewModel: ProductoDetalleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(productoId) {
        viewModel.onEvent(ProductoDetalleUiEvent.LoadProducto(productoId))
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                ProductoDetalleEfecto.NavigateBack -> navController.popBackStack()
            }
        }
    }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            if (it.isNotBlank()) snack.showSnackbar(it)
            viewModel.onEvent(ProductoDetalleUiEvent.UserMessageShown)
        }
    }

    ProductoDetalleScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = { navController.popBackStack() },
        snack = snack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetalleScreenContent(
    state: ProductoDetalleUiState,
    onEvent: (ProductoDetalleUiEvent) -> Unit,
    onBackClick: () -> Unit,
    snack: SnackbarHostState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.isEditMode) "Editar Producto" else "Detalle del Producto",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    if (!state.isEditMode) {
                        IconButton(onClick = { onEvent(ProductoDetalleUiEvent.ToggleEditMode) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snack) },
        floatingActionButton = {
            if (state.isEditMode) {
                FloatingActionButton(
                    onClick = { onEvent(ProductoDetalleUiEvent.Save) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Guardar"
                    )
                }
            }
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    ProductImage(
                        imageUrl = state.productoImagenUrl,
                        contentDescription = state.productoNombre,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholderSize = 64.dp
                    )
                }

                OutlinedTextField(
                    value = state.productoNombre,
                    onValueChange = { onEvent(ProductoDetalleUiEvent.NombreChanged(it)) },
                    label = { Text("Nombre del Producto") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isEditMode,
                    isError = state.productoNombreError != null,
                    supportingText = {
                        state.productoNombreError?.let { Text(text = it) }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                OutlinedTextField(
                    value = state.categoria,
                    onValueChange = { onEvent(ProductoDetalleUiEvent.CategoriaChanged(it)) },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isEditMode,
                    isError = state.categoriaError != null,
                    supportingText = {
                        state.categoriaError?.let { Text(text = it) }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = state.productoMonto,
                        onValueChange = { onEvent(ProductoDetalleUiEvent.MontoChanged(it)) },
                        label = { Text("Precio (RD$)") },
                        modifier = Modifier.weight(1f),
                        enabled = state.isEditMode,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.productoMontoError != null,
                        supportingText = {
                            state.productoMontoError?.let { Text(text = it) }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    OutlinedTextField(
                        value = state.productoCantidad,
                        onValueChange = { onEvent(ProductoDetalleUiEvent.CantidadChanged(it)) },
                        label = { Text("Cantidad") },
                        modifier = Modifier.weight(1f),
                        enabled = state.isEditMode,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.productoCantidadError != null,
                        supportingText = {
                            state.productoCantidadError?.let { Text(text = it) }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                OutlinedTextField(
                    value = state.productoDescripcion,
                    onValueChange = { onEvent(ProductoDetalleUiEvent.DescripcionChanged(it)) },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    enabled = state.isEditMode,
                    maxLines = 5,
                    isError = state.productoDescripcionError != null,
                    supportingText = {
                        state.productoDescripcionError?.let { Text(text = it) }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Información adicional",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        if (state.fecha.isNotBlank()) {
                            Text(
                                text = "Fecha: ${state.fecha}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (state.productoId != null) {
                            Text(
                                text = "ID: ${state.productoId}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (state.isEditMode) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onEvent(ProductoDetalleUiEvent.ToggleEditMode) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = { onEvent(ProductoDetalleUiEvent.Save) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}
