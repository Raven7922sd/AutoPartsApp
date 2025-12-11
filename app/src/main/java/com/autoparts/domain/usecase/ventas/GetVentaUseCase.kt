package com.autoparts.domain.usecase.ventas

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.Venta
import com.autoparts.domain.repository.VentasRepository
import javax.inject.Inject

class GetVentaUseCase @Inject constructor(
    private val repository: VentasRepository
) {
    suspend operator fun invoke(id: Int): Resource<Venta> {
        return repository.getVenta(id)
    }
}