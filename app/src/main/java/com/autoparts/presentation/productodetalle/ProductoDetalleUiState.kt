package com.autoparts.presentation.productodetalle

data class ProductoDetalleUiState(
    val productoId: Int? = null,
    val isLoading: Boolean = false,

    // Campos principales
    val productoNombre: String = "",
    val productoNombreError: String? = null,
    val productoMonto: String = "",
    val productoMontoError: String? = null,
    val productoCantidad: String = "",
    val productoCantidadError: String? = null,
    val productoDescripcion: String = "",
    val productoDescripcionError: String? = null,

    // Categoría
    val categoria: String = "",
    val categoriaError: String? = null,

    // Imagen y fecha
    val productoImagenUrl: String = "",
    val fecha: String = "",

    // Estado UI
    val isEditMode: Boolean = false,
    val userMessage: String? = null
)
