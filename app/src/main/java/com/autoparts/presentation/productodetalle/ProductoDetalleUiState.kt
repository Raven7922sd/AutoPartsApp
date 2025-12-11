package com.autoparts.presentation.productodetalle

data class ProductoDetalleUiState(
    val productoId: Int? = null,
    val isLoading: Boolean = false,
    val productoNombre: String = "",
    val productoNombreError: String? = null,
    val productoMonto: String = "",
    val productoMontoError: String? = null,
    val productoCantidad: String = "",
    val productoCantidadError: String? = null,
    val productoDescripcion: String = "",
    val productoDescripcionError: String? = null,
    val categoria: String = "",
    val categoriaError: String? = null,
    val productoImagenUrl: String = "",
    val fecha: String = "",
    val isEditMode: Boolean = false,
    val userMessage: String? = null,
    val cantidadParaCarrito: Int = 1,
    val isAddingToCarrito: Boolean = false
)