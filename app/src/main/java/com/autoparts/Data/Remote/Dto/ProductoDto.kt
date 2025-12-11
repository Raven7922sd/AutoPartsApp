package com.autoparts.Data.Remote.Dto

data class ProductoDto(
    val productoId: Int?,
    val productoNombre: String,
    val productoMonto: Int,
    val productoCantidad: Int,
    val productoDescripcion: String,
    val productoImagenUrl: String,
    val categoria: String,
    val fecha: String
)
