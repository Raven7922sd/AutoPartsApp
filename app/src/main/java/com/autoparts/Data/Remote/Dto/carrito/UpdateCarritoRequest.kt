package com.autoparts.data.remote.dto.carrito

import com.google.gson.annotations.SerializedName

data class UpdateCarritoRequest(
    @SerializedName("cantidad")
    val cantidad: Int
)