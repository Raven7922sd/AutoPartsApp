package com.autoparts.data.remote.mapper

import com.autoparts.data.remote.dto.cita.CitaDto
import com.autoparts.domain.model.Cita

object CitaMapper {
    fun toDomain(dto: CitaDto): Cita {
        return Cita(
            citaId = dto.citaId,
            clienteNombre = dto.clienteNombre,
            applicationUserId = dto.applicationUserId,
            servicioSolicitado = dto.servicioSolicitado,
            fechaCita = dto.fechaCita,
            confirmada = dto.confirmada,
            codigoConfirmacion = dto.codigoConfirmacion
        )
    }
}