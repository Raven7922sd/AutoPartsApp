package com.autoparts.domain.usecase.ventas

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.EstadisticasVentas
import com.autoparts.domain.repository.VentasRepository
import javax.inject.Inject

class GetEstadisticasVentasUseCase @Inject constructor(
    private val repository: VentasRepository
) {
    suspend operator fun invoke(
        fechaDesde: String? = null,
        fechaHasta: String? = null
    ): Resource<EstadisticasVentas> {
        return repository.getEstadisticas(fechaDesde, fechaHasta)
    }
}