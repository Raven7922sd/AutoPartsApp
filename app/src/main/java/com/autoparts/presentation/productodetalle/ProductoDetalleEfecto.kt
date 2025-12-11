package com.autoparts.presentation.productodetalle

sealed interface ProductoDetalleEfecto {
    data object NavigateBack : ProductoDetalleEfecto
}

