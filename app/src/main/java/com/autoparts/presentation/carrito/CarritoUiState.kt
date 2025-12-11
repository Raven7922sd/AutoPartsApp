package com.autoparts.presentation.carrito

import com.autoparts.dominio.model.Carrito
import com.autoparts.dominio.model.CarritoTotal

data class CarritoUiState(
    val isLoading: Boolean = false,
    val carritoItems: List<Carrito> = emptyList(),
    val carritoTotal: CarritoTotal = CarritoTotal(),
    val error: String? = null,
    val successMessage: String? = null,
    val isUserLoggedIn: Boolean = false
)