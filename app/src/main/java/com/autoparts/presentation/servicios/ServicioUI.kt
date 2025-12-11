package com.autoparts.presentation.servicios

import android.graphics.Bitmap

data class ServicioUI(
    val servicioId: Int,
    val nombre: String,
    val precioFormateado: String,
    val descripcion: String,
    val duracionFormateada: String,
    val imagen: Bitmap? = null,
    val solicitados: Int,
    val fechaServicio: String
)