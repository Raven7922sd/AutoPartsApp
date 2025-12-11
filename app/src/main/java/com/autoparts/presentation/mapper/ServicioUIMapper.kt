package com.autoparts.presentation.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.autoparts.domain.model.Servicio
import com.autoparts.presentation.servicios.ServicioUI

object ServicioUIMapper {

    fun toUI(servicio: Servicio): ServicioUI {
        return ServicioUI(
            servicioId = servicio.servicioId,
            nombre = servicio.nombre,
            precioFormateado = formatearPrecio(servicio.precio),
            descripcion = servicio.descripcion,
            duracionFormateada = formatearDuracion(servicio.duracionEstimada),
            imagen = decodificarImagen(servicio.servicioImagenBase64),
            solicitados = servicio.solicitados,
            fechaServicio = servicio.fechaServicio
        )
    }

    fun toUIList(servicios: List<Servicio>): List<ServicioUI> {
        return servicios.map { toUI(it) }
    }

    private fun formatearPrecio(precio: Double): String {
        return "RD$ ${String.format("%,.2f", precio)}"
    }

    private fun formatearDuracion(duracion: Double): String {
        return if (duracion < 1) {
            "${(duracion * 60).toInt()} minutos"
        } else {
            "${duracion.toInt()} ${if (duracion == 1.0) "hora" else "horas"}"
        }
    }

    private fun decodificarImagen(base64: String?): Bitmap? {
        return base64?.let {
            try {
                val bytes = Base64.decode(it, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                null
            }
        }
    }
}