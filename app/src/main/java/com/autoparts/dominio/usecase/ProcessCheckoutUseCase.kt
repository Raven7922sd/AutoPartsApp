package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.CreateVenta
import com.autoparts.dominio.model.Venta
import com.autoparts.dominio.repository.VentasRepository
import javax.inject.Inject

class ProcessCheckoutUseCase @Inject constructor(
    private val repository: VentasRepository
) {
    suspend operator fun invoke(createVenta: CreateVenta): Resource<Venta> {
        return repository.processCheckout(createVenta)
    }
}