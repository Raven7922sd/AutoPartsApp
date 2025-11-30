package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.model.UpdateCarrito
import com.autoparts.dominio.repository.CarritoRepository
import javax.inject.Inject

class UpdateCarritoItemUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(carritoId: Int, updateCarrito: UpdateCarrito): Resource<Unit> {
        return repository.updateItem(carritoId, updateCarrito)
    }
}