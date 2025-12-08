package com.autoparts.domain.usecase.carrito

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.UpdateCarrito
import com.autoparts.domain.repository.CarritoRepository
import javax.inject.Inject

class UpdateCarritoItemUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(carritoId: Int, updateCarrito: UpdateCarrito): Resource<Unit> {
        return repository.updateCarritoItem(carritoId, updateCarrito)
    }
}