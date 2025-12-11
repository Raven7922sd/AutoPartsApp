package com.autoparts.data.remote.dto.producto

import com.google.gson.annotations.SerializedName

data class ProductoDto(
    @SerializedName("productoId")
    val productoId: Int?,
    @SerializedName("productoNombre")
    val productoNombre: String,
    @SerializedName("productoMonto")
    val productoMonto: Int,
    @SerializedName("productoCantidad")
    val productoCantidad: Int,
    @SerializedName("productoDescripcion")
    val productoDescripcion: String,
    @SerializedName("productoImagenUrl")
    val productoImagenUrl: String,
    @SerializedName("categoria")
    val categoria: String,
    @SerializedName("fecha")
    val fecha: String
)