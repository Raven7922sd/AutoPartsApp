package com.autoparts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.autoparts.presentation.Inicio.HomeScreen
import com.autoparts.presentation.Login.LoginScreen
import com.autoparts.presentation.productos.ProductosScreen
import com.autoparts.presentation.categorias.CategoriasScreen
import com.autoparts.presentation.productodetalle.ProductoDetalleScreen
import com.autoparts.presentation.carrito.CarritoScreen
import com.autoparts.presentation.checkout.CheckoutScreen
import com.autoparts.presentation.ventas.VentasScreen
import com.autoparts.presentation.ventadetalle.VentaDetalleScreen
import com.autoparts.presentation.perfil.PerfilScreen


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
            HomeScreen(
                navController,
                userId = id as String?
            )
        }
        composable("home_screen") {
            HomeScreen(
                navController,
                userId = null
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
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
                    navController.navigate(Screen.Login.route)
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
            PerfilScreen(navController = navController)
        }
    }
}