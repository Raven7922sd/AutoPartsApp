package com.autoparts.data.remote.dto.cita

import com.google.gson.annotations.SerializedName

data class CitaDto(
    @SerializedName("citaId")
    val citaId: Int,
    @SerializedName("clienteNombre")
    val clienteNombre: String,
    @SerializedName("applicationUserId")
    val applicationUserId: String,
    @SerializedName("servicioSolicitado")
    val servicioSolicitado: String,
    @SerializedName("fechaCita")
    val fechaCita: String,
    @SerializedName("confirmada")
    val confirmada: Boolean,
    @SerializedName("codigoConfirmacion")
    val codigoConfirmacion: String
)