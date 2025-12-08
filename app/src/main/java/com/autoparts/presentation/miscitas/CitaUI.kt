package com.autoparts.presentation.miscitas

data class CitaUI(
    val citaId: Int,
    val clienteNombre: String,
    val servicioSolicitado: String,
    val fechaFormateada: String,
    val horaFormateada: String,
    val fechaCompleta: String,
    val confirmada: Boolean,
    val estadoTexto: String,
    val estadoColor: Long,
    val codigoConfirmacion: String
)