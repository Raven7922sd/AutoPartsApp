package com.autoparts.presentation.productos

sealed interface ProductosEfecto {
    data class NavigateToDetalle(val productoId: Int) : ProductosEfecto
}

