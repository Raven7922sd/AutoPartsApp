package com.autoparts.data.remote.dto.servicio

import com.google.gson.annotations.SerializedName

data class ServicioDto(
    @SerializedName("servicioId")
    val servicioId: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("precio")
    val precio: Double,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("duracionEstimada")
    val duracionEstimada: Double,
    @SerializedName("servicioImagenBase64")
    val servicioImagenBase64: String?,
    @SerializedName("solicitados")
    val solicitados: Int,
    @SerializedName("fechaServicio")
    val fechaServicio: String
)