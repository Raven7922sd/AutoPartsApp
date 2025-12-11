package com.autoparts.presentation.mapper

import com.autoparts.domain.model.Cita
import com.autoparts.presentation.miscitas.CitaUI
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object CitaUIMapper {

    fun toUI(cita: Cita): CitaUI {
        return CitaUI(
            citaId = cita.citaId,
            clienteNombre = cita.clienteNombre,
            servicioSolicitado = cita.servicioSolicitado,
            fechaFormateada = formatearFecha(cita.fechaCita),
            horaFormateada = formatearHora(cita.fechaCita),
            fechaCompleta = formatearFechaCompleta(cita.fechaCita),
            confirmada = cita.confirmada,
            estadoTexto = getEstadoTexto(cita.confirmada),
            estadoColor = getEstadoColor(cita.confirmada),
            codigoConfirmacion = cita.codigoConfirmacion
        )
    }

    fun toUIList(citas: List<Cita>): List<CitaUI> {
        return citas.map { toUI(it) }
    }

    private fun formatearFecha(fechaISO: String): String {
        return try {
            val zdt = ZonedDateTime.parse(fechaISO)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("es-DO"))
            zdt.format(formatter)
        } catch (e: Exception) {
            fechaISO
        }
    }

    private fun formatearHora(fechaISO: String): String {
        return try {
            val zdt = ZonedDateTime.parse(fechaISO)
            val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.forLanguageTag("es-DO"))
            zdt.format(formatter)
        } catch (e: Exception) {
            ""
        }
    }

    private fun formatearFechaCompleta(fechaISO: String): String {
        return try {
            val zdt = ZonedDateTime.parse(fechaISO)
            val formatter = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy 'a las' hh:mm a", Locale.forLanguageTag("es-DO"))
            zdt.format(formatter)
        } catch (e: Exception) {
            fechaISO
        }
    }

    private fun getEstadoTexto(confirmada: Boolean): String {
        return if (confirmada) "Confirmada" else "Pendiente"
    }

    private fun getEstadoColor(confirmada: Boolean): Long {
        return if (confirmada) 0xFF4CAF50 else 0xFFFFA726
    }
}