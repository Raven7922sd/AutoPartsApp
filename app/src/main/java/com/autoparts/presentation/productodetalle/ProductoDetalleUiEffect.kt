package com.autoparts.presentation.productodetalle

sealed interface ProductoDetalleUiEffect {
    data class ShowMessage(val message: String) : ProductoDetalleUiEffect
    object NavigateBack : ProductoDetalleUiEffect
}