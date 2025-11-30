package com.autoparts.dominio.usecase

import com.autoparts.Data.Remote.Resource
import com.autoparts.dominio.repository.CarritoRepository
import javax.inject.Inject

class DeleteCarritoItemUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(carritoId: Int): Resource<Unit> {
        return repository.deleteItem(carritoId)
    }
}