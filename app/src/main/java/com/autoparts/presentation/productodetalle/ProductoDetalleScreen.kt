package com.autoparts.presentation.productodetalle

import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
                is ProductoDetalleUiEffect.NavigateBack -> navController.popBackStack()
                is ProductoDetalleUiEffect.ShowMessage -> snack.showSnackbar(effect.message)
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            ProductoDetalleTopBar(onBackClick = onBackClick)
        },
        snackbarHost = { SnackbarHost(hostState = snack) }
    ) { padding ->
        ProductoDetalleContent(
            state = state,
            onEvent = onEvent,
            padding = padding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductoDetalleTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Detalle del Producto",
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun ProductoDetalleContent(
    state: ProductoDetalleUiState,
    onEvent: (ProductoDetalleUiEvent) -> Unit,
    padding: PaddingValues
) {
    when {
        state.isLoading -> LoadingIndicator(padding)
        else -> ProductoDetalleForm(state, onEvent, padding)
    }
}

@Composable
private fun LoadingIndicator(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ProductoDetalleForm(
    state: ProductoDetalleUiState,
    onEvent: (ProductoDetalleUiEvent) -> Unit,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProductoImageCard(imageUrl = state.producto?.productoImagenUrl, nombre = state.producto?.productoNombre)
        ProductoInfoFields(state)
        AddToCarritoButton(state, onEvent)
    }
}

@Composable
private fun ProductoImageCard(imageUrl: String?, nombre: String?) {
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
            imageUrl = imageUrl ?: "",
            contentDescription = nombre ?: "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholderSize = 64.dp
        )
    }
}

@Composable
private fun ProductoInfoFields(state: ProductoDetalleUiState) {
    DisabledTextField(
        value = state.producto?.productoNombre ?: "",
        label = "Nombre del Producto"
    )

    DisabledTextField(
        value = state.producto?.categoria ?: "",
        label = "Categoría"
    )

    PrecioYCantidadRow(
        precio = state.producto?.productoMonto?.toString() ?: "",
        cantidad = state.producto?.productoCantidad?.toString() ?: ""
    )

    DisabledTextField(
        value = state.producto?.productoDescripcion ?: "",
        label = "Descripción",
        modifier = Modifier.height(120.dp),
        maxLines = 5
    )
}

@Composable
private fun DisabledTextField(
    value: String,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = { },
        label = { Text(label) },
        modifier = modifier,
        enabled = false,
        maxLines = maxLines,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun PrecioYCantidadRow(precio: String, cantidad: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DisabledTextField(
            value = precio,
            label = "Precio (RD$)",
            modifier = Modifier.weight(1f)
        )
        DisabledTextField(
            value = cantidad,
            label = "Cantidad",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AddToCarritoButton(
    state: ProductoDetalleUiState,
    onEvent: (ProductoDetalleUiEvent) -> Unit
) {
    Button(
        onClick = { state.producto?.productoId?.let { onEvent(ProductoDetalleUiEvent.AddToCarrito(it)) } },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(bottom = 32.dp),
        enabled = !state.isAddingToCarrito
    ) {
        AddToCarritoButtonContent(isAdding = state.isAddingToCarrito)
    }
}

@Composable
private fun AddToCarritoButtonContent(isAdding: Boolean) {
    when {
        isAdding -> {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        }
        else -> {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Agregar al carrito",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}