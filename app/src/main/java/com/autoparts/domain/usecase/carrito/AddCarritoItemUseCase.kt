package com.autoparts.domain.usecase.carrito

import com.autoparts.data.remote.util.Resource
import com.autoparts.domain.model.AddCarrito
import com.autoparts.domain.model.Carrito
import com.autoparts.domain.repository.CarritoRepository
import javax.inject.Inject

class AddCarritoItemUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(addCarrito: AddCarrito): Resource<Carrito> {
        return repository.addCarritoItem(addCarrito)
    }
}