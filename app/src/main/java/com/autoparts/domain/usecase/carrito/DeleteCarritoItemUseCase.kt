package com.autoparts.domain.usecase.carrito

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.repository.CarritoRepository
import javax.inject.Inject

class DeleteCarritoItemUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(carritoId: Int): Resource<Unit> {
        return repository.deleteCarritoItem(carritoId)
    }
}