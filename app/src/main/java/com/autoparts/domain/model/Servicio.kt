package com.autoparts.domain.model

data class Servicio(
    val servicioId: Int,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val duracionEstimada: Double,
    val servicioImagenBase64: String?,
    val solicitados: Int,
    val fechaServicio: String
)