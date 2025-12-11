package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.repository.CarritoRepository
import javax.inject.Inject

class ClearCarritoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.clearCarrito()
    }
}