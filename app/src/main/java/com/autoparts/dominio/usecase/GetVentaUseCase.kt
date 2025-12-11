package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.Venta
import com.autoparts.dominio.repository.VentasRepository
import javax.inject.Inject

class GetVentaUseCase @Inject constructor(
    private val repository: VentasRepository
) {
    suspend operator fun invoke(ventaId: Int): Resource<Venta> {
        return repository.getVenta(ventaId)
    }
}