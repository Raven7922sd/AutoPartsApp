package com.autoparts.presentation.productos

import com.autoparts.dominio.model.Producto

data class ProductosUiState(
    val isLoading: Boolean = false,
    val listProductos: List<Producto> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val userMessage: String? = null
)
