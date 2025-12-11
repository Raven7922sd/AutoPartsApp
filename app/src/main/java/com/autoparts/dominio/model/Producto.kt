package com.autoparts.dominio.model

data class Producto(
    val productoId: Int?,
    val productoNombre: String,
    val productoMonto: Int,
    val productoCantidad: Int,
    val productoDescripcion: String,
    val productoImagenUrl: String,
    val categoria: String,
    val fecha: String
)
