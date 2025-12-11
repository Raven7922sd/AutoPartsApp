package com.autoparts.presentation.carrito

sealed interface CarritoUiEvent {
    data object LoadCarrito : CarritoUiEvent
    data class AddItem(val productoId: Int, val cantidad: Int) : CarritoUiEvent
    data class UpdateItem(val carritoId: Int, val cantidad: Int) : CarritoUiEvent
    data class DeleteItem(val carritoId: Int) : CarritoUiEvent
    data object ClearCarrito : CarritoUiEvent
    data object ClearError : CarritoUiEvent
    data object ClearSuccess : CarritoUiEvent
}