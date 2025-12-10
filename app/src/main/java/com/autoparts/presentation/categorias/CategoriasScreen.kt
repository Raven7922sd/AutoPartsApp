package com.autoparts.presentation.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autoparts.domain.model.Producto
import com.autoparts.presentation.inicio.InicioViewModel
import com.autoparts.presentation.components.ProductImage
import java.util.Locale

private const val CATEGORIA_USO_GENERAL = "Uso General"
private const val CATEGORIA_AUTOS = "Autos o Vehículos Ligeros"
private const val CATEGORIA_MOTOCICLETAS = "Motocicletas"
private const val CATEGORIA_VEHICULOS_PESADOS = "Vehículos Pesados"
private const val PRICE_MIN_DEFAULT = 0
private const val PRICE_MAX_DEFAULT = 1000000

private fun matchesSearchQuery(producto: Producto, query: String): Boolean {
    if (query.isBlank()) return true
    return producto.productoNombre.contains(query, ignoreCase = true) ||
           producto.categoria.contains(query, ignoreCase = true) ||
           producto.productoDescripcion.contains(query, ignoreCase = true)
}

private fun matchesCategory(producto: Producto, category: String?): Boolean {
    return category?.let { producto.categoria.equals(it, ignoreCase = true) } ?: true
}

private fun matchesPriceRange(producto: Producto, minPrice: Int, maxPrice: Int): Boolean {
    return producto.productoMonto in minPrice..maxPrice
}

private fun hasActiveFilters(
    selectedCategory: String?,
    minPrice: Int,
    maxPrice: Int,
    searchQuery: String
): Boolean {
    return selectedCategory != null ||
           minPrice > PRICE_MIN_DEFAULT ||
           maxPrice < PRICE_MAX_DEFAULT ||
           searchQuery.isNotEmpty()
}

private fun handleBottomNavigation(
    index: Int,
    onNavigateToHome: () -> Unit,
    onNavigateToServicios: () -> Unit,
    onNavigateToCarrito: () -> Unit
) {
    when (index) {
        0 -> onNavigateToHome()
        2 -> onNavigateToServicios()
        3 -> onNavigateToCarrito()
    }
}

private fun resetAllFilters(
    onCategoryChange: (String?) -> Unit,
    onMinPriceChange: (Int) -> Unit,
    onMaxPriceChange: (Int) -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    onCategoryChange(CATEGORIA_USO_GENERAL)
    onMinPriceChange(PRICE_MIN_DEFAULT)
    onMaxPriceChange(PRICE_MAX_DEFAULT)
    onSearchQueryChange("")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    onNavigateToProductoDetalle: (Int) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToServicios: () -> Unit,
    onNavigateToCarrito: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: InicioViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(CATEGORIA_USO_GENERAL) }
    var minPrice by remember { mutableIntStateOf(PRICE_MIN_DEFAULT) }
    var maxPrice by remember { mutableIntStateOf(PRICE_MAX_DEFAULT) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val categorias = listOf(
        CATEGORIA_USO_GENERAL,
        CATEGORIA_AUTOS,
        CATEGORIA_MOTOCICLETAS,
        CATEGORIA_VEHICULOS_PESADOS
    )

    val filteredProducts = state.listProductos.filter { producto ->
        matchesCategory(producto, selectedCategory) &&
        matchesPriceRange(producto, minPrice, maxPrice) &&
        matchesSearchQuery(producto, searchQuery)
    }

    val isPriceFiltered = minPrice > PRICE_MIN_DEFAULT || maxPrice < PRICE_MAX_DEFAULT

    Scaffold(
        topBar = {
            CategoriasTopBar(
                onNavigateBack = onNavigateBack,
                isPriceFiltered = isPriceFiltered,
                onFilterClick = { showFilterDialog = true }
            )
        },
        bottomBar = {
            Surface(modifier = Modifier.padding(bottom = 32.dp)) {
                com.autoparts.presentation.inicio.BottomNavigationBar(
                    selectedItem = 1,
                    onItemSelected = { index ->
                        handleBottomNavigation(index, onNavigateToHome, onNavigateToServicios, onNavigateToCarrito)
                    }
                )
            }
        }
    ) { padding ->
        CategoriasContent(
            padding = padding,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            categorias = categorias,
            selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                selectedCategory = if (selectedCategory == category) null else category
            },
            filteredProducts = filteredProducts,
            hasActiveFilters = hasActiveFilters(selectedCategory, minPrice, maxPrice, searchQuery),
            onClearFilters = {
                resetAllFilters(
                    { selectedCategory = it },
                    { minPrice = it },
                    { maxPrice = it },
                    { searchQuery = it }
                )
            },
            onNavigateToProductoDetalle = onNavigateToProductoDetalle
        )
    }

    if (showFilterDialog) {
        PriceFilterDialog(
            minPrice = minPrice,
            maxPrice = maxPrice,
            onMinPriceChange = { minPrice = it },
            onMaxPriceChange = { maxPrice = it },
            onDismiss = { showFilterDialog = false },
            onApply = { showFilterDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoriasTopBar(
    onNavigateBack: () -> Unit,
    isPriceFiltered: Boolean,
    onFilterClick: () -> Unit
) {
    TopAppBar(
        title = { Text("Categorías") },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
            }
        },
        actions = {
            IconButton(onClick = onFilterClick) {
                Badge(
                    containerColor = if (isPriceFiltered)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Icon(Icons.Default.FilterList, "Filtrar por precio")
                }
            }
        }
    )
}

@Composable
private fun CategoriasContent(
    padding: PaddingValues,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    categorias: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
    filteredProducts: List<Producto>,
    hasActiveFilters: Boolean,
    onClearFilters: () -> Unit,
    onNavigateToProductoDetalle: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        SearchBar(searchQuery, onSearchQueryChange)

        CategoryFilterRow(
            categorias = categorias,
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        ProductsHeader(
            productsCount = filteredProducts.size,
            hasActiveFilters = hasActiveFilters,
            onClearFilters = onClearFilters
        )

        ProductsContentView(filteredProducts, onNavigateToProductoDetalle)
    }
}

@Composable
private fun SearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Buscar productos...") },
        leadingIcon = { Icon(Icons.Default.Search, "Buscar") },
        trailingIcon = { ClearSearchButton(searchQuery) { onSearchQueryChange("") } },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}

@Composable
private fun ProductsHeader(
    productsCount: Int,
    hasActiveFilters: Boolean,
    onClearFilters: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$productsCount productos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        ClearFiltersButton(visible = hasActiveFilters, onClick = onClearFilters)
    }
}

@Composable
private fun ProductsContentView(
    filteredProducts: List<Producto>,
    onNavigateToProductoDetalle: (Int) -> Unit
) {
    when {
        filteredProducts.isEmpty() -> EmptyProductsView()
        else -> ProductsListView(filteredProducts, onNavigateToProductoDetalle)
    }
}

@Composable
private fun ClearSearchButton(searchQuery: String, onClear: () -> Unit) {
    if (searchQuery.isNotEmpty()) {
        IconButton(onClick = onClear) {
            Icon(Icons.Default.Close, "Limpiar")
        }
    }
}

@Composable
private fun ClearFiltersButton(visible: Boolean, onClick: () -> Unit) {
    if (visible) {
        TextButton(onClick = onClick) {
            Text("Limpiar filtros")
        }
    }
}

@Composable
private fun EmptyProductsView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Sin resultados",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No se encontraron productos",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Intenta con otros filtros",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProductsListView(
    productos: List<Producto>,
    onProductoClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = productos,
            key = { producto -> producto.productoId ?: 0 }
        ) { producto ->
            ProductoFilteredCard(
                producto = producto,
                onProductoClick = {
                    producto.productoId?.let { id -> onProductoClick(id) }
                }
            )
        }
    }
}

@Composable
fun CategoryFilterRow(
    categorias: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categorias) { categoria ->
            CategoryChip(
                categoria = categoria,
                isSelected = categoria == selectedCategory,
                onClick = { onCategorySelected(categoria) }
            )
        }
    }
}

@Composable
fun CategoryChip(
    categoria: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (categoria) {
                        CATEGORIA_USO_GENERAL -> Icons.Default.Build
                        CATEGORIA_AUTOS -> Icons.Default.DirectionsCar
                        CATEGORIA_MOTOCICLETAS -> Icons.Default.TwoWheeler
                        CATEGORIA_VEHICULOS_PESADOS -> Icons.Default.LocalShipping
                        else -> Icons.Default.Category
                    },
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = categoria,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
            }
        },
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Seleccionado",
                    modifier = Modifier.size(18.dp)
                )
            }
        } else null
    )
}

@Composable
fun ProductoFilteredCard(
    producto: Producto,
    onProductoClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProductoClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProductImage(
                imageUrl = producto.productoImagenUrl,
                contentDescription = producto.productoNombre,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop,
                placeholderSize = 48.dp
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = producto.productoNombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = producto.categoria,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$${String.format(Locale.US, "%,d", producto.productoMonto)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory,
                        contentDescription = "Stock",
                        modifier = Modifier.size(16.dp),
                        tint = if (producto.productoCantidad > 10)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "${producto.productoCantidad} disponibles",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun adjustMaxPriceOnMinChange(minPrice: Int): Int = when {
    minPrice >= 10000 -> PRICE_MAX_DEFAULT
    minPrice >= 1000 -> 10000
    minPrice >= 500 -> 1000
    else -> 1000
}

private fun adjustMinPriceOnMaxChange(maxPrice: Int): Int = when {
    maxPrice <= 1000 -> PRICE_MIN_DEFAULT
    maxPrice <= 5000 -> 1000
    maxPrice <= 10000 -> 5000
    else -> 10000
}

private fun formatPriceRange(minPrice: Int, maxPrice: Int): String = when {
    minPrice == PRICE_MIN_DEFAULT && maxPrice == PRICE_MAX_DEFAULT -> "Todos los precios"
    minPrice == PRICE_MIN_DEFAULT -> "Hasta RD$ ${String.format(Locale.US, "%,d", maxPrice)}"
    maxPrice == PRICE_MAX_DEFAULT -> "Desde RD$ ${String.format(Locale.US, "%,d", minPrice)}"
    else -> "RD$ ${String.format(Locale.US, "%,d", minPrice)} - RD$ ${String.format(Locale.US, "%,d", maxPrice)}"
}

private fun handleMinPriceSelection(
    price: Int,
    currentMaxPrice: Int,
    onMinPriceChange: (Int) -> Unit,
    onMaxPriceChange: (Int) -> Unit
) {
    onMinPriceChange(price)
    if (price > currentMaxPrice && price > PRICE_MIN_DEFAULT) {
        onMaxPriceChange(adjustMaxPriceOnMinChange(price))
    }
}

private fun handleMaxPriceSelection(
    price: Int,
    currentMinPrice: Int,
    onMinPriceChange: (Int) -> Unit,
    onMaxPriceChange: (Int) -> Unit
) {
    onMaxPriceChange(price)
    if (price < currentMinPrice && price < PRICE_MAX_DEFAULT) {
        onMinPriceChange(adjustMinPriceOnMaxChange(price))
    }
}

@Composable
fun PriceFilterDialog(
    minPrice: Int,
    maxPrice: Int,
    onMinPriceChange: (Int) -> Unit,
    onMaxPriceChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    var selectedMinPrice by remember { mutableIntStateOf(minPrice) }
    var selectedMaxPrice by remember { mutableIntStateOf(maxPrice) }

    val priceOptions = listOf(
        0 to "Sin mínimo",
        100 to "RD$ 100",
        500 to "RD$ 500",
        1000 to "RD$ 1,000",
        10000 to "RD$ 10,000+"
    )

    val maxPriceOptions = listOf(
        1000 to "RD$ 1,000",
        5000 to "RD$ 5,000",
        10000 to "RD$ 10,000",
        50000 to "RD$ 50,000",
        1000000 to "Sin máximo"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtrar por Precio") },
        text = {
            PriceFilterDialogContent(
                priceOptions = priceOptions,
                maxPriceOptions = maxPriceOptions,
                selectedMinPrice = selectedMinPrice,
                selectedMaxPrice = selectedMaxPrice,
                onMinPriceSelect = { price ->
                    handleMinPriceSelection(
                        price,
                        selectedMaxPrice,
                        { selectedMinPrice = it },
                        { selectedMaxPrice = it }
                    )
                },
                onMaxPriceSelect = { price ->
                    handleMaxPriceSelection(
                        price,
                        selectedMinPrice,
                        { selectedMinPrice = it },
                        { selectedMaxPrice = it }
                    )
                }
            )
        },
        confirmButton = {
            Button(onClick = {
                onMinPriceChange(selectedMinPrice)
                onMaxPriceChange(selectedMaxPrice)
                onApply()
            }) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun PriceFilterDialogContent(
    priceOptions: List<Pair<Int, String>>,
    maxPriceOptions: List<Pair<Int, String>>,
    selectedMinPrice: Int,
    selectedMaxPrice: Int,
    onMinPriceSelect: (Int) -> Unit,
    onMaxPriceSelect: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MinPriceSection(priceOptions, selectedMinPrice, onMinPriceSelect)
        HorizontalDivider()
        MaxPriceSection(maxPriceOptions, selectedMaxPrice, onMaxPriceSelect)
        PriceRangeSummary(selectedMinPrice, selectedMaxPrice)
    }
}

@Composable
private fun MinPriceSection(
    priceOptions: List<Pair<Int, String>>,
    selectedPrice: Int,
    onPriceSelect: (Int) -> Unit
) {
    Text(
        text = "Precio mínimo",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )
    PriceOptionsRow(priceOptions, selectedPrice, onPriceSelect)
}

@Composable
private fun MaxPriceSection(
    priceOptions: List<Pair<Int, String>>,
    selectedPrice: Int,
    onPriceSelect: (Int) -> Unit
) {
    Text(
        text = "Precio máximo",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
    )
    PriceOptionsRow(priceOptions, selectedPrice, onPriceSelect)
}

@Composable
private fun PriceOptionsRow(
    options: List<Pair<Int, String>>,
    selectedPrice: Int,
    onPriceSelect: (Int) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { (price, label) ->
            PriceFilterChip(
                price = price,
                label = label,
                isSelected = selectedPrice == price,
                onClick = { onPriceSelect(price) }
            )
        }
    }
}

@Composable
private fun PriceFilterChip(
    price: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = if (isSelected) {
            { Icon(Icons.Default.CheckCircle, null, Modifier.size(18.dp)) }
        } else null
    )
}

@Composable
private fun PriceRangeSummary(minPrice: Int, maxPrice: Int) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Rango seleccionado:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = formatPriceRange(minPrice, maxPrice),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}