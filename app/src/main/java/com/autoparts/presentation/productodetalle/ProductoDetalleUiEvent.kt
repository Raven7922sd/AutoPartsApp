package com.autoparts.presentation.productodetalle

interface ProductoDetalleUiEvent {
    data class LoadProducto(val productoId: Int) : ProductoDetalleUiEvent
    data class NombreChanged(val nombre: String) : ProductoDetalleUiEvent
    data class MontoChanged(val monto: String) : ProductoDetalleUiEvent
    data class CantidadChanged(val cantidad: String) : ProductoDetalleUiEvent
    data class DescripcionChanged(val descripcion: String) : ProductoDetalleUiEvent
    data class CategoriaChanged(val categoria: String) : ProductoDetalleUiEvent
    data object Save : ProductoDetalleUiEvent
    data object ToggleEditMode : ProductoDetalleUiEvent
    data object UserMessageShown : ProductoDetalleUiEvent
}

