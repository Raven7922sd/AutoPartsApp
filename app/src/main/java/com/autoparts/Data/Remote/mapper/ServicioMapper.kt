package com.autoparts.data.remote.mapper

import com.autoparts.data.remote.dto.servicio.ServicioDto
import com.autoparts.domain.model.Servicio

object ServicioMapper {
    fun toDomain(dto: ServicioDto): Servicio {
        return Servicio(
            servicioId = dto.servicioId,
            nombre = dto.nombre,
            precio = dto.precio,
            descripcion = dto.descripcion,
            duracionEstimada = dto.duracionEstimada,
            servicioImagenBase64 = dto.servicioImagenBase64,
            solicitados = dto.solicitados,
            fechaServicio = dto.fechaServicio
        )
    }
}