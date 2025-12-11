package com.autoparts.presentation.Inicio

import com.autoparts.dominio.model.Usuarios
import com.autoparts.dominio.model.Producto

data class CarritoItem(
    val producto: Producto,
    val cantidad: Int
)

data class InicioUiState(
    val userId: String? = null,
    val isLoadingUser: Boolean = false,
    val isLoadingList: Boolean = false,
    val isLoadingProductos: Boolean = false,
    val listUsuarios: List<Usuarios> = emptyList(),
    val listProductos: List<Producto> = emptyList(),
    val email: String = "",
    val emailError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val userMessage: String? = null,
    val showDialog: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val categorias: List<String> = listOf("Frenos", "Suspensión", "Motor", "Transmisión", "Eléctrico"),
    val carritoLocal: List<CarritoItem> = emptyList()
)