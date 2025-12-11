package com.autoparts.presentation.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autoparts.R
import com.autoparts.domain.model.Producto
import com.autoparts.presentation.components.ProductImage
import com.autoparts.ui.theme.AutoPartsAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    viewModel: InicioViewModel = hiltViewModel(),
    onNavigateToCarrito: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToProductoDetalle: (Int) -> Unit,
    onNavigateToCategoria: (String) -> Unit,
    onNavigateToCategorias: () -> Unit,
    onNavigateToServicios: () -> Unit,
    onNavigateToVentas: () -> Unit,
    onNavigateToMisCitas: () -> Unit,
    onNavigateToAdminCitas: () -> Unit = {},
    onNavigateToAdminVentas: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is InicioUiEffect.NavigateToCarrito -> onNavigateToCarrito()
                is InicioUiEffect.NavigateToLogin -> onNavigateToLogin()
                is InicioUiEffect.NavigateToPerfil -> onNavigateToPerfil()
                is InicioUiEffect.NavigateToVentas -> onNavigateToVentas()
                is InicioUiEffect.NavigateToMisCitas -> onNavigateToMisCitas()
                is InicioUiEffect.NavigateToAdminCitas -> onNavigateToAdminCitas()
                is InicioUiEffect.NavigateToAdminVentas -> onNavigateToAdminVentas()
                is InicioUiEffect.NavigateToCategorias -> onNavigateToCategorias()
                is InicioUiEffect.NavigateToServicios -> onNavigateToServicios()
                is InicioUiEffect.NavigateToProductoDetalle -> onNavigateToProductoDetalle(effect.productoId)
                is InicioUiEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    InicioScreenContent(
        uiState = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        onNavigateToCarrito = onNavigateToCarrito,
        onNavigateToPerfil = onNavigateToPerfil,
        onNavigateToLogin = onNavigateToLogin,
        onNavigateToCategorias = onNavigateToCategorias,
        onNavigateToServicios = onNavigateToServicios
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreenContent(
    uiState: InicioUiState,
    onEvent: (InicioUiEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    onNavigateToCarrito: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToCategorias: () -> Unit,
    onNavigateToServicios: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showUserMenu by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            InicioDrawerContent(
                isAdmin = uiState.isAdmin,
                drawerState = drawerState,
                onEvent = onEvent
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                InicioTopBar(
                    isLoggedIn = uiState.isLoggedIn,
                    showUserMenu = showUserMenu,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onCarritoClick = onNavigateToCarrito,
                    onUserMenuToggle = { showUserMenu = !showUserMenu },
                    onUserMenuDismiss = { showUserMenu = false },
                    onNavigateToPerfil = onNavigateToPerfil,
                    onNavigateToLogin = onNavigateToLogin,
                    onCerrarSesion = { onEvent(InicioUiEvent.OnCerrarSesion) }
                )
            },
            bottomBar = {
                Surface(modifier = Modifier.padding(bottom = 32.dp)) {
                    BottomNavigationBar(
                        selectedItem = 0,
                        onItemSelected = { index ->
                            handleBottomNavigation(index, onNavigateToCategorias, onNavigateToServicios, onNavigateToCarrito)
                        }
                    )
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { padding ->
            InicioContentList(
                uiState = uiState,
                onEvent = onEvent,
                padding = padding
            )
        }
    }
}

@Composable
private fun InicioDrawerContent(
    isAdmin: Boolean,
    drawerState: DrawerState,
    onEvent: (InicioUiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            DrawerHeader()
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            UserNavigationItems(
                drawerState = drawerState,
                onEvent = onEvent
            )

            if (isAdmin) {
                AdminNavigationSection(
                    drawerState = drawerState,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun DrawerHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.autoparts_redondo_bien),
            contentDescription = "AutoParts",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "AutoParts",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun UserNavigationItems(
    drawerState: DrawerState,
    onEvent: (InicioUiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()

    NavigationDrawerItem(
        icon = { Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null) },
        label = { Text("Mis Compras") },
        selected = false,
        onClick = {
            scope.launch { drawerState.close() }
            onEvent(InicioUiEvent.OnNavigateToVentas)
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    NavigationDrawerItem(
        icon = { Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null) },
        label = { Text("Mis Citas") },
        selected = false,
        onClick = {
            scope.launch { drawerState.close() }
            onEvent(InicioUiEvent.OnNavigateToMisCitas)
        }
    )
}

@Composable
private fun AdminNavigationSection(
    drawerState: DrawerState,
    onEvent: (InicioUiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()

    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Panel de Administrador",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        label = { Text("Gestionar Citas") },
        selected = false,
        onClick = {
            scope.launch { drawerState.close() }
            onEvent(InicioUiEvent.OnNavigateToAdminCitas)
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Default.Receipt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        label = { Text("Ver Todas las Ventas") },
        selected = false,
        onClick = {
            scope.launch { drawerState.close() }
            onEvent(InicioUiEvent.OnNavigateToAdminVentas)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InicioTopBar(
    isLoggedIn: Boolean,
    showUserMenu: Boolean,
    onMenuClick: () -> Unit,
    onCarritoClick: () -> Unit,
    onUserMenuToggle: () -> Unit,
    onUserMenuDismiss: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.autoparts_redondo_bien),
                    contentDescription = "AutoParts",
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "AutoParts",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            IconButton(onClick = onCarritoClick) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            UserMenuDropdown(
                isLoggedIn = isLoggedIn,
                showUserMenu = showUserMenu,
                onUserMenuToggle = onUserMenuToggle,
                onUserMenuDismiss = onUserMenuDismiss,
                onNavigateToPerfil = onNavigateToPerfil,
                onNavigateToLogin = onNavigateToLogin,
                onCerrarSesion = onCerrarSesion
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun UserMenuDropdown(
    isLoggedIn: Boolean,
    showUserMenu: Boolean,
    onUserMenuToggle: () -> Unit,
    onUserMenuDismiss: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Box {
        IconButton(onClick = onUserMenuToggle) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        DropdownMenu(
            expanded = showUserMenu,
            onDismissRequest = onUserMenuDismiss
        ) {
            if (isLoggedIn) {
                LoggedInMenuItems(
                    onNavigateToPerfil = {
                        onUserMenuDismiss()
                        onNavigateToPerfil()
                    },
                    onCerrarSesion = {
                        onUserMenuDismiss()
                        onCerrarSesion()
                    }
                )
            } else {
                LoggedOutMenuItem(
                    onNavigateToLogin = {
                        onUserMenuDismiss()
                        onNavigateToLogin()
                    }
                )
            }
        }
    }
}

@Composable
private fun LoggedInMenuItems(
    onNavigateToPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    DropdownMenuItem(
        text = { Text("Mi Perfil") },
        onClick = onNavigateToPerfil,
        leadingIcon = { Icon(Icons.Default.Person, "Mi Perfil") }
    )
    HorizontalDivider()
    DropdownMenuItem(
        text = { Text("Cerrar Sesión") },
        onClick = onCerrarSesion,
        leadingIcon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, "Cerrar Sesión") }
    )
}

@Composable
private fun LoggedOutMenuItem(onNavigateToLogin: () -> Unit) {
    DropdownMenuItem(
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Iniciar Sesión")
            }
        },
        onClick = onNavigateToLogin
    )
}

@Composable
private fun InicioContentList(
    uiState: InicioUiState,
    onEvent: (InicioUiEvent) -> Unit,
    padding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { onEvent(InicioUiEvent.OnSearchQueryChanged(it)) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        when {
            uiState.searchQuery.isNotBlank() -> {
                displaySearchResults(uiState, onEvent)
            }
            else -> {
                displayMainContent(uiState, onEvent)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun LazyListScope.displaySearchResults(
    uiState: InicioUiState,
    onEvent: (InicioUiEvent) -> Unit
) {
    val productosFiltrados = filterProductos(uiState.productosParaMostrar, uiState.searchQuery)

    when {
        productosFiltrados.isNotEmpty() -> {
            item {
                Text(
                    text = "Resultados de búsqueda (${productosFiltrados.size})",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            items(
                items = productosFiltrados,
                key = { it.productoId ?: 0 }
            ) { producto ->
                ProductoCardWithImage(
                    producto = producto,
                    onProductoClick = { onEvent(InicioUiEvent.OnProductoClick(producto.productoId ?: 0)) },
                    onAddToCarrito = { onEvent(InicioUiEvent.OnAddToCarrito(producto.productoId ?: 0)) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        else -> {
            item { EmptySearchResult() }
        }
    }
}

private fun LazyListScope.displayMainContent(
    uiState: InicioUiState,
    onEvent: (InicioUiEvent) -> Unit
) {
    item {
        CategoriesSection(
            productos = uiState.productosParaMostrar,
            onEvent = onEvent
        )
    }

    item {
        Text(
            text = "Nuestros Productos",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    items(
        items = uiState.productosParaMostrar,
        key = { it.productoId ?: 0 }
    ) { producto ->
        ProductoCardWithImage(
            producto = producto,
            onProductoClick = { onEvent(InicioUiEvent.OnProductoClick(producto.productoId ?: 0)) },
            onAddToCarrito = { onEvent(InicioUiEvent.OnAddToCarrito(producto.productoId ?: 0)) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun EmptySearchResult() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No se encontraron productos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Intenta con otra búsqueda",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CategoriesSection(
    productos: List<Producto>,
    onEvent: (InicioUiEvent) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Categorias",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        val categorias = listOf(
            "Uso General",
            "Autos o Vehículos Ligeros",
            "Motocicletas",
            "Vehículos Pesados"
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categorias) { categoria ->
                val productoCategoria = productos.firstOrNull {
                    it.categoria.equals(categoria, ignoreCase = true)
                }
                productoCategoria?.let {
                    CategoryProductCard(
                        producto = it,
                        categoria = categoria,
                        onProductoClick = {
                            onEvent(InicioUiEvent.OnProductoClick(productoCategoria.productoId ?: 0))
                        }
                    )
                }
            }
        }
    }
}

private fun filterProductos(productos: List<Producto>, query: String): List<Producto> {
    return productos.filter {
        it.productoNombre.contains(query, ignoreCase = true) ||
        it.categoria.contains(query, ignoreCase = true) ||
        it.productoDescripcion.contains(query, ignoreCase = true)
    }
}

private fun handleBottomNavigation(
    index: Int,
    onNavigateToCategorias: () -> Unit,
    onNavigateToServicios: () -> Unit,
    onNavigateToCarrito: () -> Unit
) {
    when (index) {
        1 -> onNavigateToCategorias()
        2 -> onNavigateToServicios()
        3 -> onNavigateToCarrito()
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Buscar productos") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar"
            )
        },
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true
    )
}

@Composable
fun CategoryProductCard(
    producto: Producto,
    categoria: String,
    onProductoClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(180.dp)
            .clickable(onClick = onProductoClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ProductImage(
                imageUrl = producto.productoImagenUrl,
                contentDescription = producto.productoNombre,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholderSize = 64.dp
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = categoria,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        text = producto.productoNombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "RD$ ${String.format(java.util.Locale.US, "%,d", producto.productoMonto)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoCardWithImage(
    producto: Producto,
    onProductoClick: () -> Unit,
    onAddToCarrito: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onProductoClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
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
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
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

                    Text(
                        text = producto.categoria,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "RD$ ${String.format(java.util.Locale.US, "%,d", producto.productoMonto)}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = onAddToCarrito,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Agregar",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agregar")
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        modifier = Modifier.height(64.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        val iconModifier = Modifier.size(20.dp)
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio", modifier = iconModifier) },
            label = { Text("Inicio", style = MaterialTheme.typography.labelSmall) },
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) }
        )
        NavigationBarItem(
                icon = { Icon(Icons.Default.Menu, contentDescription = "Categorías", modifier = iconModifier) },
            label = { Text("Categorías", style = MaterialTheme.typography.labelSmall) },
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Build, contentDescription = "Servicios", modifier = iconModifier) },
            label = { Text("Servicios", style = MaterialTheme.typography.labelSmall) },
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", modifier = iconModifier) },
            label = { Text("Carrito", style = MaterialTheme.typography.labelSmall) },
            selected = selectedItem == 3,
            onClick = { onItemSelected(3) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    AutoPartsAppTheme {
        InicioScreenContent(
            uiState = InicioUiState(
                productos = listOf(
                    Producto(
                        productoId = 1,
                        productoNombre = "Aceite Mobil 1",
                        productoMonto = 1500,
                        productoCantidad = 10,
                        productoDescripcion = "Aceite sintético premium",
                        productoImagenUrl = "",
                        categoria = "Uso General",
                        fecha = "2024-01-01"
                    )
                ),
                isLoading = false,
                isLoggedIn = false,
                searchQuery = ""
            ),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
            onNavigateToCarrito = {},
            onNavigateToPerfil = {},
            onNavigateToLogin = {},
            onNavigateToCategorias = {},
            onNavigateToServicios = {}
        )
    }
}