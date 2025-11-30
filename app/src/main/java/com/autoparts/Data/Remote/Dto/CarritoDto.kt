package com.autoparts.Data.Remote.Dto

data class CarritoDto(
    val carritoId: Int = 0,
    val applicationUserId: String = "",
    val productoId: Int = 0,
    val producto: ProductoDto? = null,
    val cantidad: Int = 0
)

data class CarritoTotalDto(
    val totalItems: Int = 0,
    val totalPrice: Double = 0.0
)

data class AddCarritoDto(
    val productoId: Int,
    val cantidad: Int
)

data class UpdateCarritoDto(
    val cantidad: Int
)

