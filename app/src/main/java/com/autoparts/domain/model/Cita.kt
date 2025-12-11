package com.autoparts.domain.model

data class Cita(
    val citaId: Int,
    val clienteNombre: String,
    val applicationUserId: String,
    val servicioSolicitado: String,
    val fechaCita: String,
    val confirmada: Boolean,
    val codigoConfirmacion: String
)

data class CreateCita(
    val clienteNombre: String,
    val servicioSolicitado: String,
    val fechaCita: String
)