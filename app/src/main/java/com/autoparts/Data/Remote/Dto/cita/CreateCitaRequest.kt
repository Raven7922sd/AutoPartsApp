package com.autoparts.data.remote.dto.cita

import com.google.gson.annotations.SerializedName

data class CreateCitaRequest(
    @SerializedName("clienteNombre")
    val clienteNombre: String,
    @SerializedName("servicioSolicitado")
    val servicioSolicitado: String,
    @SerializedName("fechaCita")
    val fechaCita: String
)