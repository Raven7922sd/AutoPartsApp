package com.autoparts.domain.usecase.ventas

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.VentasPaginadas
import com.autoparts.domain.repository.VentasRepository
import javax.inject.Inject

class GetAllVentasUseCase @Inject constructor(
    private val repository: VentasRepository
) {
    suspend operator fun invoke(
        pagina: Int = 1,
        tamanoPagina: Int = 10,
        fechaDesde: String? = null,
        fechaHasta: String? = null,
        usuarioId: String? = null
    ): Resource<VentasPaginadas> {
        return repository.getAllVentas(
            pagina, tamanoPagina, fechaDesde, fechaHasta, usuarioId
        )
    }
}