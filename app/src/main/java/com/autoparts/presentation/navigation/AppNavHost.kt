package com.autoparts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.autoparts.presentation.Inicio.HomeScreen
import com.autoparts.presentation.Login.LoginScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    androidx.navigation.NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(
            route = Screen.Home.route,
            arguments = listOf(
                navArgument(Screen.Home.ARG) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt(Screen.Home.ARG)
            HomeScreen(
                navController,
                usuarioId = id
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
    }

}
