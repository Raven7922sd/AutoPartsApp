package com.autoparts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.autoparts.presentation.inicio.InicioScreen
import com.autoparts.presentation.login.LoginScreen
import com.autoparts.presentation.productos.ProductosScreen
import com.autoparts.presentation.categorias.CategoriasScreen
import com.autoparts.presentation.productodetalle.ProductoDetalleScreen
import com.autoparts.presentation.carrito.CarritoScreen
import com.autoparts.presentation.checkout.CheckoutScreen
import com.autoparts.presentation.ventas.VentasScreen
import com.autoparts.presentation.ventadetalle.VentaDetalleScreen
import com.autoparts.presentation.perfil.PerfilScreen
import com.autoparts.presentation.servicios.ServiciosScreen
import com.autoparts.presentation.serviciodetalle.ServicioDetalleScreen
import com.autoparts.presentation.miscitas.MisCitasScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = "home_screen",
        modifier = modifier
    ){
        composable(
            route = Screen.Home.route,
            arguments = listOf(
                navArgument(Screen.Home.ARG){
                    type = NavType.IntType
                    defaultValue = -1
                    nullable = false
                }
            )
        ) {backStackEntry ->
            val id = backStackEntry.arguments?.getInt(Screen.Home.ARG)?.takeIf { it != -1 }
            InicioScreen(
                onNavigateToCarrito = { navController.navigate(Screen.Carrito.route) },
                onNavigateToLogin = { navController.navigate(Screen.Login.createRoute()) },
                onNavigateToPerfil = { navController.navigate(Screen.Perfil.route) },
                onNavigateToProductoDetalle = { productoId ->
                    navController.navigate(Screen.ProductoDetalle.createRoute(productoId))
                },
                onNavigateToCategoria = { categoria ->
                    navController.navigate(Screen.Categorias.route)
                },
                onNavigateToCategorias = { navController.navigate(Screen.Categorias.route) },
                onNavigateToServicios = { navController.navigate(Screen.Servicios.route) },
                onNavigateToVentas = { navController.navigate(Screen.Ventas.route) },
                onNavigateToMisCitas = { navController.navigate(Screen.MisCitas.route) },
                onNavigateToAdminCitas = { navController.navigate(Screen.AdminCitas.route) },
                onNavigateToAdminVentas = { navController.navigate(Screen.AdminVentas.route) }
            )
        }
        composable("home_screen") {
            InicioScreen(
                onNavigateToCarrito = { navController.navigate(Screen.Carrito.route) },
                onNavigateToLogin = { navController.navigate(Screen.Login.createRoute()) },
                onNavigateToPerfil = { navController.navigate(Screen.Perfil.route) },
                onNavigateToProductoDetalle = { productoId ->
                    navController.navigate(Screen.ProductoDetalle.createRoute(productoId))
                },
                onNavigateToCategoria = { categoria ->
                    navController.navigate(Screen.Categorias.route)
                },
                onNavigateToCategorias = { navController.navigate(Screen.Categorias.route) },
                onNavigateToServicios = { navController.navigate(Screen.Servicios.route) },
                onNavigateToVentas = { navController.navigate(Screen.Ventas.route) },
                onNavigateToMisCitas = { navController.navigate(Screen.MisCitas.route) },
                onNavigateToAdminCitas = { navController.navigate(Screen.AdminCitas.route) },
                onNavigateToAdminVentas = { navController.navigate(Screen.AdminVentas.route) }
            )
        }
        composable(
            route = Screen.Login.route,
            arguments = listOf(
                navArgument(Screen.Login.ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val message = backStackEntry.arguments?.getString(Screen.Login.ARG)
            LoginScreen(navController, initialMessage = message)
        }
        composable(Screen.Productos.route) {
            ProductosScreen(navController)
        }
        composable(Screen.Categorias.route) {
            CategoriasScreen(navController)
        }
        composable(
            route = Screen.ProductoDetalle.route,
            arguments = listOf(
                navArgument(Screen.ProductoDetalle.ARG){type = NavType.IntType}
            )
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt(Screen.ProductoDetalle.ARG) ?: 0
            ProductoDetalleScreen(
                navController = navController,
                productoId = productoId
            )
        }
        composable(Screen.Carrito.route) {
            CarritoScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCheckout = {
                    navController.navigate(Screen.Checkout.route)
                },
                onNavigateToLogin = {
                    navController.navigate(
                        Screen.Login.createRoute("Inicie sesión para realizar la compra")
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.Checkout.route) {
            CheckoutScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVenta = { ventaId ->
                    navController.navigate(Screen.VentaDetalle.createRoute(ventaId)) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }
        composable(Screen.Ventas.route) {
            VentasScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVenta = { ventaId ->
                    navController.navigate(Screen.VentaDetalle.createRoute(ventaId))
                },
                onNavigateToLogin = {
                    navController.navigate(
                        Screen.Login.createRoute("Inicie sesión para ver su historial de compras")
                    ) {
                        popUpTo(Screen.Ventas.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Screen.VentaDetalle.route,
            arguments = listOf(
                navArgument(Screen.VentaDetalle.ARG) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val ventaId = backStackEntry.arguments?.getInt(Screen.VentaDetalle.ARG) ?: 0
            VentaDetalleScreen(
                ventaId = ventaId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Perfil.route) {
            PerfilScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVentas = {
                    navController.navigate(Screen.Ventas.route)
                },
                onNavigateToMisCitas = {
                    navController.navigate(Screen.MisCitas.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.createRoute()) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }
        composable(Screen.Servicios.route) {
            ServiciosScreen(navController = navController)
        }
        composable(
            route = Screen.ServicioDetalle.route,
            arguments = listOf(
                navArgument(Screen.ServicioDetalle.ARG) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val servicioId = backStackEntry.arguments?.getInt(Screen.ServicioDetalle.ARG) ?: 0
            ServicioDetalleScreen(
                navController = navController,
                servicioId = servicioId
            )
        }
        composable(
            route = Screen.CrearCita.route,
            arguments = listOf(
                navArgument(Screen.CrearCita.ARG) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val servicioId = backStackEntry.arguments?.getInt(Screen.CrearCita.ARG) ?: 0
            com.autoparts.presentation.crearcita.CrearCitaScreen(
                navController = navController,
                servicioId = servicioId
            )
        }
        composable(Screen.MisCitas.route) {
            MisCitasScreen(navController = navController)
        }

        // Rutas de administrador
        composable(Screen.AdminCitas.route) {
            com.autoparts.presentation.admin.citas.AdminCitasScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.createRoute("Debes iniciar sesión como administrador"))
                }
            )
        }
        composable(Screen.AdminVentas.route) {
            com.autoparts.presentation.admin.ventas.AdminVentasScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.createRoute("Debes iniciar sesión como administrador"))
                }
            )
        }
    }
}