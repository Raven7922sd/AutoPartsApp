package com.autoparts.domain.usecase.ventas

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.CreateVenta
import com.autoparts.domain.model.Venta
import com.autoparts.domain.repository.VentasRepository
import javax.inject.Inject

class ProcessCheckoutUseCase @Inject constructor(
    private val repository: VentasRepository
) {
    suspend operator fun invoke(createVenta: CreateVenta): Resource<Venta> {
        return repository.processCheckout(createVenta)
    }
}