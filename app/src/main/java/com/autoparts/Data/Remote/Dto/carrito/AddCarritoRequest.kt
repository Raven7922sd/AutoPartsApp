package com.autoparts.data.remote.dto.carrito

import com.google.gson.annotations.SerializedName

data class AddCarritoRequest(
    @SerializedName("productoId")
    val productoId: Int,
    @SerializedName("cantidad")
    val cantidad: Int
)