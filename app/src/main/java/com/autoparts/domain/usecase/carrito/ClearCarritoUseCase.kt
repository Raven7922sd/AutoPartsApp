package com.autoparts.domain.usecase.carrito

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.repository.CarritoRepository
import javax.inject.Inject

class ClearCarritoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.clearCarrito()
    }
}