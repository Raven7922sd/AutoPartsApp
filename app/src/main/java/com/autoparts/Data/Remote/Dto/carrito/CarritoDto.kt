package com.autoparts.data.remote.dto.carrito

import com.google.gson.annotations.SerializedName
import com.autoparts.data.remote.dto.producto.ProductoDto

data class CarritoDto(
    @SerializedName("carritoId")
    val carritoId: Int,
    @SerializedName("applicationUserId")
    val applicationUserId: String,
    @SerializedName("productoId")
    val productoId: Int,
    @SerializedName("producto")
    val producto: ProductoDto?,
    @SerializedName("cantidad")
    val cantidad: Int
)