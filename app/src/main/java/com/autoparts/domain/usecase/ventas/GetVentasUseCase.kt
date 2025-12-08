package com.autoparts.domain.usecase.ventas

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Venta
import com.autoparts.domain.repository.VentasRepository
import javax.inject.Inject

class GetVentasUseCase @Inject constructor(
    private val repository: VentasRepository
) {
    suspend operator fun invoke(): Resource<List<Venta>> {
        return repository.getVentas()
    }
}