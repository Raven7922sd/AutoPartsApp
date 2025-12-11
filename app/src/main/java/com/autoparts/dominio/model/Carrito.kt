package com.autoparts.dominio.model

data class Carrito(
    val carritoId: Int = 0,
    val applicationUserId: String = "",
    val productoId: Int = 0,
    val producto: Producto? = null,
    val cantidad: Int = 0
)

data class CarritoTotal(
    val totalItems: Int = 0,
    val totalPrice: Double = 0.0
)

data class AddCarrito(
    val productoId: Int,
    val cantidad: Int
)

data class UpdateCarrito(
    val cantidad: Int
)
