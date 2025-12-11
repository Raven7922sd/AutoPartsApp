package com.autoparts.data.remote.dto.carrito

import com.google.gson.annotations.SerializedName

data class CarritoTotalResponse(
    @SerializedName("totalItems")
    val totalItems: Int,
    @SerializedName("totalPrice")
    val totalPrice: Double
)